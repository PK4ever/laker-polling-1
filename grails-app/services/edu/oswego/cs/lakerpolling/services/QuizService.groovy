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
        User requestingUser = token?.user
        if (!requestingUser || !quizIdString.isLong()) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }

        def choices = commaSeparatedStrToList(choiceString)
        def answers = toBooleanList(answerString)
        Quiz quiz = Quiz.findById(quizIdString.toLong())
        if (!quiz || !choices || !answers) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }

        Course course = quiz.course
        if (!courseService.hasInstructorAccess(requestingUser, course)) {
            return QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
        }

        //TODO add choices to question
        Question question = new Question(course: course, question: text, answers: answers)
        quiz.addToQuestions(question)
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
