package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.domains.AuthToken

import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.domains.Question
import edu.oswego.cs.lakerpolling.domains.Quiz
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.QueryResult
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

@Transactional
class QuizService {
    CourseService courseService

    /**
     * Gets a list of all of the question IDs for a quiz.
     * @param token - The access token of the requesting user
     * @param quizIdString - the id of the quiz
     * @return a QueryResult containing the IDs of all of the questions in the given quiz
     */
    QueryResult<List<Long>> getAllQuestionIds(AuthToken token, String quizIdString) {
        QueryResult<List<Long>> result = new QueryResult<>()
        def quizResult = findQuiz(quizIdString)
        if (!quizResult.success) {
            return QueryResult.copyError(quizResult)
        }

        def quiz = quizResult.data
        def accessCheck = verifyStudentAccess(token, quiz)
        if (!accessCheck.success) {
            return QueryResult.copyError(accessCheck)
        }

        result.data = quiz.questions.collect { question -> question.id }
        result
    }

    /**
     * Gets the question with the given id of the specified quiz
     * @param access_token - The access token of the requesting user
     * @param quizIdString - the id of the quiz
     * @param questionIdString - the id of the question
     * @return a QueryResult containing the given question in the specified quiz
     */
    QueryResult<Question> getQuestion(AuthToken token, String quizIdString, String questionIdString) {
        QueryResult<List<Long>> result = new QueryResult<>()
        def quizResult = findQuiz(quizIdString)
        if (!quizResult.success) {
            return QueryResult.copyError(quizResult)
        }

        def quiz = quizResult.data
        def accessCheck = verifyStudentAccess(token, quiz)
        if (!accessCheck.success) {
            return QueryResult.copyError(accessCheck)
        }
        findQuestion(quiz, questionIdString)
    }

    /**
     * Creates a question and adds it to the quiz with the given ID
     * @param token - the token to use to retrieve the requesting user.
     * @param quizIdString - a String representing the id of the quiz
     * @param text - the answer text of the question
     * @param choiceString - a comma-separated String containing the text of the answer options
     * @param answerString - a comma-separated String of Boolean values representing whether each option in the question is a correct choice or not.
     * @return The results of the operations.
     */
    QueryResult<Question> postQuestionToQuiz(AuthToken token, String quizIdString, String text, String choiceString, String answerString) {
        QueryResult<Question> result = new QueryResult<>()
        def choices = commaSeparatedStrToList(choiceString)
        def answers = toBooleanList(answerString)
        if (!choices || !answers) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }

        def quizResult = findQuiz(quizIdString)
        if (!quizResult.success) {
            return QueryResult.copyError(quizResult)
        }

        def quiz = quizResult.data
        def accessCheck = verifyInstructorAccess(token, quiz)
        if (!accessCheck.success) {
            return QueryResult.copyError(accessCheck)
        }

        Question question = new Question(course: quiz.course, question: text, choices: choices, answers: answers)
        quiz.addToQuestions(question)
        result.data = question
        result
    }

    /**
     * Returns a successfull Query Result if the user represented in token has student access to the given quiz
     * @param token - the Authtoken
     * @param quiz - the quiz
     * @return a QueryResult representing the result of the check
     */
    private QueryResult verifyStudentAccess(AuthToken token, Quiz quiz) {
        def userResult = findUser(token)
        if (!userResult.success) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }

        def user = userResult.data
        def course = quiz.course
        if (!courseService.hasStudentAccess(user, course)) {
            return QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
        }
        new QueryResult(success:true)
    }

    /**
     * Returns a successful Query Result if the user represented in token has instructor access to the given quiz
     * @param token - the Authtoken
     * @param quiz - the quiz
     * @return a QueryResult representing the result of the check
     */
    private QueryResult verifyInstructorAccess(AuthToken token, Quiz quiz) {
        def userResult = findUser(token)
        if (!userResult.success) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }

        def user = userResult.data
        def course = quiz.course
        if (!courseService.hasInstructorAccess(user, course)) {
            return QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
        }
        new QueryResult(success:true)
    }

    /**
     * Attempts to find the user associated with the given AuthToken
     * @param token - the AuthToken
     * @return A QueryResult containing the associated user
     */
    private QueryResult<User> findUser(AuthToken token) {
        QueryResult<Question> result = new QueryResult<>()
        User user = token?.user
        if (!user) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }
        result.data = user
        result
    }

    /**
     * Attempts to find the Quiz associated with the given ID String
     * @param quizIdString - A String representing a Quiz ID
     * @return A QueryResult containing the associated Quiz
     */
    private QueryResult<Quiz> findQuiz(String quizIdString) {
        QueryResult<Quiz> result = new QueryResult<>()
        if (!quizIdString.isLong()) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }
        Quiz quiz = Quiz.findById(quizIdString.toLong())
        if (!quiz) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }
        result.data = quiz
        result
    }

    /**
     * Attempts to find the Question associated with the given quiz and question ID string
     * @param quiz - the quiz
     * @param questionIdString - a String representing a question ID
     * @return A QueryResult containing the associated Question
     */
    private QueryResult<Question> findQuestion(Quiz quiz, String questionIdString) {
        QueryResult<Question> result = new QueryResult<>()
        if (!questionIdString.isLong()) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }

        def questionId = questionIdString.toLong()
        Set<Question> questions = quiz.questions

        def question = questions.find { question -> question.id == questionId }
        if (!question) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }

        result.data = question
        result
    }

    private List<Boolean> toBooleanList(String commaSeparatedStr) {
        List<Boolean> boolList = new ArrayList<>()
        def stringList = commaSeparatedStrToList(commaSeparatedStr)
        stringList.forEach {s ->
            if(s == "false") boolList.add(false)
            else if(s == "true") boolList.add(true)
            else return null
        }
        return boolList
    }

    private List<String> commaSeparatedStrToList(String str) {
        str.indexOf(",") != -1 ? str.split(",").toList(): [str]
    }

}
