package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.domains.Answer
import edu.oswego.cs.lakerpolling.domains.AuthToken

import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.domains.Grade
import edu.oswego.cs.lakerpolling.domains.Question
import edu.oswego.cs.lakerpolling.domains.Quiz
import edu.oswego.cs.lakerpolling.domains.QuizSubmission
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.QueryResult
import edu.oswego.cs.lakerpolling.util.QuestionType
import edu.oswego.cs.lakerpolling.util.RoleType
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat

@Transactional
class QuizService {
    CourseService courseService
    QuestionService questionService

    /**
     * Creates a quiz
     * @param token - the access token of the requesting user
     * @param courseIdString - a String representation of the course ID
     * @param name - (optional) the name of the quiz
     * @param startTimestamp - a String representation of a UNIX timestamp for the starting time of the quiz
     * @param endTimestamp - a String representation of a UNIX timestamp for the ending time of the quiz
     * @return a Query Result containing the created Quiz
     */
    QueryResult<Quiz> createQuiz(AuthToken token, String courseIdString, String name, String startTimestamp, String endTimestamp) {
        QueryResult<Quiz> result = new QueryResult<>()

        Date startDate = parseTimestamp(startTimestamp)
        Date endDate = parseTimestamp(endTimestamp)
        def today = new Date().clearTime()
        if (!startDate || !endDate || endDate < startDate || endDate < today) {
            result = QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
            result.message = "Invalid Start or End Date"
            return result
        }

        def courseResult = courseService.findCourse(courseIdString)
        if (!courseResult.success) {
            return QueryResult.copyError(courseResult)
        }

        def course = courseResult.data
        def accessCheck = courseService.verifyInstructorAccess(token, course)
        if (!accessCheck.success) {
            return QueryResult.copyError(accessCheck)
        }

        Quiz quiz = new Quiz(course: course, name: name, startDate: startDate, endDate: endDate)
        quiz.save(flush: true, failOnError: true)
        result.data = quiz
        result
    }

    /**
     * Gets a list of all of the quizzes for the course
     * @param token - The access token of the requesting user
     * @param courseIdString - the id of the course
     * @return a QueryResult containing a list of all of the quizzes in the given course
     */
    QueryResult<List<Quiz>> getAllQuizzes(AuthToken token, String courseIdString) {
        QueryResult<List<Quiz>> result = new QueryResult<>()
        def courseResult = courseService.findCourse(courseIdString)
        if (!courseResult.success) {
            return QueryResult.copyError(courseResult)
        }

        Course course = courseResult.data
        def accessCheck = courseService.verifyStudentAccess(token, course)
        if (!accessCheck.success) {
            return QueryResult.copyError(accessCheck)
        }

        result.data = Quiz.where {
            course == course
        }.list()
        result
    }

    /**
     * Gets the quiz with the given ID
     * @param token - The access token of the requesting user
     * @param quizIdString - the id of the quiz
     * @return a QueryResult containing the Quiz
     */
    QueryResult<Quiz> getQuiz(AuthToken token, String quizIdString) {
        QueryResult<Quiz> result = new QueryResult<>()
        def quizResult = findQuiz(quizIdString)
        if (!quizResult.success) {
            return QueryResult.copyError(quizResult)
        }

        Quiz quiz = quizResult.data
        def accessCheck = courseService.verifyStudentAccess(token, quiz.course)
        if (!accessCheck.success) {
            return QueryResult.copyError(accessCheck)
        }

        result.data = quiz
        result
    }

    /**
     * Deletes the quiz with the given ID
     * @param token - The access token of the requesting user
     * @param quizIdString - the id of the quiz
     * @return a QueryResult containing the results of the operation
     */
    QueryResult deleteQuiz(AuthToken token, String quizIdString) {
        def quizResult = findQuiz(quizIdString)
        if (!quizResult.success) {
            return QueryResult.copyError(quizResult)
        }

        Quiz quiz = quizResult.data
        def accessCheck = courseService.verifyInstructorAccess(token, quiz.course)
        if (!accessCheck.success) {
            return QueryResult.copyError(accessCheck)
        }

        quiz.delete(flush: true, failOnError: true)
        new QueryResult(success: true)
    }

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
        def accessCheck = courseService.verifyStudentAccess(token, quiz.course)
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
        def quizResult = findQuiz(quizIdString)
        if (!quizResult.success) {
            return QueryResult.copyError(quizResult)
        }

        def quiz = quizResult.data
        def course = quiz.course
        def accessCheck = courseService.verifyStudentAccess(token, course)
        if (!accessCheck.success) {
            return QueryResult.copyError(accessCheck)
        }

        // Prevent Students from viewing question while the quiz is closed
        if (!quizIsOpen(quiz) && !courseService.verifyInstructorAccess(token, course).success) {
            return QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
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
        def accessCheck = courseService.verifyInstructorAccess(token, quiz.course)
        if (!accessCheck.success) {
            return QueryResult.copyError(accessCheck)
        }

        Question question = new Question(course: quiz.course, question: text, choices: choices, answers: answers, type: QuestionType.QUIZ)
        quiz.addToQuestions(question)
        result.data = question
        result
    }

    /**
     * Removes a question from a quiz
     * @param token - the token of the requesting user
     * @param quizIdString - a String representing the ID of the quiz
     * @param questionIdString - A String representing the ID of the question
     * @return the results of the operation
     */
    QueryResult deleteQuestion(AuthToken token, String quizIdString, String questionIdString) {
        def quizResult = findQuiz(quizIdString)
        if (!quizResult.success) {
            return QueryResult.copyError(quizResult)
        }

        def quiz = quizResult.data
        def accessCheck = courseService.verifyInstructorAccess(token, quiz.course)
        if (!accessCheck.success) {
            return QueryResult.copyError(accessCheck)
        }

        def questionResult = findQuestion(quiz, questionIdString)
        if (!questionResult.success) {
            return QueryResult.copyError(questionResult)
        }

        def question = questionResult.data
        quiz.removeFromQuestions(question)
        question.delete(flush: true, failOnError: true)

        new QueryResult(success: true)
    }

    /**
     * Answers a question from a quiz
     * @param token - the token of the requesting user
     * @param quizIdString - a String representing the ID of the quiz
     * @param questionIdString - A String representing the ID of the question
     * @param responseString - A comma-separated string of Boolean values
     * @return the results of the operation
     */
    QueryResult answerQuestion(AuthToken token, String quizIdString, String questionIdString, String responseString) {
        def response = toBooleanList(responseString)
        if (!response) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }

        def quizResult = findQuiz(quizIdString)
        if (!quizResult.success) {
            return QueryResult.copyError(quizResult)
        }

        def quiz = quizResult.data
        if (!quizIsOpen(quiz)) {
            def error = QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
            error.message = "Quiz is not open"
            return error
        }

        def accessCheck = courseService.verifyStudentAccess(token, quiz.course)
        if (!accessCheck.success) {
            return QueryResult.copyError(accessCheck)
        }

        if (quizSubmissionExists(quiz, token.user)) {
            def error = QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
            error.message = "Quiz has already been submitted"
            return error
        }

        def questionResult = findQuestion(quiz, questionIdString)
        if (!questionResult.success) {
            return QueryResult.copyError(questionResult)
        }

        def question = questionResult.data
        def isCorrect = questionService.questionResponseIsCorrect(response, question)

        def answer = Answer.findByQuestionAndStudent(question, token.user)
        if (answer) {
            answer.correct = isCorrect
            answer.answers = response
        } else {
            answer = new Answer(correct: isCorrect, question: question, student: token.user, answers: response)
        }

        answer.save(flush: true, failOnError: true)
        new QueryResult(success: true)
    }

    /**
     * Creates a quiz submission for the user with the given AuthToken and the quiz with the given ID
     * @param token - the token of the requesting user
     * @param quizIdString - a String representing the id of the quiz
     * @return a QueryResult containing the created quiz submission
     */
    QueryResult<QuizSubmission> submitQuiz(AuthToken token, String quizIdString) {
        def quizResult = findQuiz(quizIdString)
        if (!quizResult.success) {
            return QueryResult.copyError(quizResult)
        }

        def quiz = quizResult.data
        def accessCheck = courseService.verifyStudentAccess(token, quiz.course)
        if (!accessCheck.success) {
            return QueryResult.copyError(accessCheck)
        }

        if (quizSubmissionExists(quiz, token.user)) {
            def error = QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
            error.message = "Quiz has already been submitted"
            return error
        }

        def studentAnswerList = new ArrayList<>()
        quiz.questions.forEach { q ->
            def answer = q.responses.find { r -> r.student == token.user }
            if (answer) {
                studentAnswerList.add(answer)
            }
        }

        def totalGrade = studentAnswerList.findAll { a -> a.correct == true }.size() / quiz.questions.size()

        new Grade(student: token.user, grade: totalGrade, quiz: quiz).save(flush: true, failOnError: true)
        def submission = new QuizSubmission(student: token.user, quiz: quiz, timestamp: new Date()).save(flush: true, failOnError: true)
        def result = new QueryResult<QuizSubmission>()
        result.data = submission
        result
    }

    /**
     * Gets a quiz submission for the user with the given AuthToken and the quiz with the given ID
     * @param token - the token of the requesting user
     * @param quizIdString - a String representing the id of the quiz
     * @return a QueryResult containing the quiz submission
     */
    QueryResult<QuizSubmission> getQuizSubmission(AuthToken token, String quizIdString) {
        def quizResult = findQuiz(quizIdString)
        if (!quizResult.success) {
            return QueryResult.copyError(quizResult)
        }

        def quiz = quizResult.data
        def accessCheck = courseService.verifyStudentAccess(token, quiz.course)
        if (!accessCheck.success) {
            return QueryResult.copyError(accessCheck)
        }

        def submission = QuizSubmission.where {
            student == token.user && quiz == quiz
        }.find()

        if (!submission) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }

        def result = new QueryResult<QuizSubmission>()
        result.data = submission
        result
    }

    def getUserGrades(AuthToken token, String quizId, String userId) {
        def user = token.user
        def result = new QueryResult<Grade>()
        result.success = false

        if (user.role.type == RoleType.INSTRUCTOR || user.role.type == RoleType.ADMIN) {
            def student = User.findById(userId.toLong())
            if (student) {
                def quiz = Quiz.findById(quizId.toLong())
                if (quiz) {
                    def quizGrades = quiz.grades.find { s -> s.student == student }
                    if (quizGrades) {
                        result.data = quizGrades
                        result.success = true
                    } else {
                        result.message = "could not find grade for selected student!"
                        result.errorCode = 400
                    }
                } else {
                    result.message = "could not find the requested quiz"
                    result.errorCode = 400
                }
            } else {
                result.message = "could not find the requested student"
                result.errorCode = 400
            }
        } else {
            result.message = "Students cannot query for grades!"
            result.errorCode = 400
        }
        result
    }

    def getQuizGrades(AuthToken token, String quizId) {
        def user = token.user
        def result = new QueryResult<List<Grade>>()
        result.success = false

        if (user.role.type == RoleType.INSTRUCTOR || user.role.type == RoleType.ADMIN) {
            def quiz = Quiz.findById(quizId.toLong())
            if (quiz) {
                result.data = quiz.grades
                result.success = true
            } else {
                result.message = "could not find quiz"
                result.errorCode = 400
            }
        } else {
            result.message = "Students cannot query for grades!"
            result.errorCode = 400
        }
        result
    }

    def gradesToCsv(AuthToken token, long courseId, HttpServletResponse response) {
        QueryResult result = new QueryResult(success: true)
        Course course = Course.findById(courseId)

        if (courseService.hasInstructorAccess(token.user, course)) {
            ServletOutputStream outputStream = response.outputStream
            List<Quiz> quizList = Quiz.createCriteria().list {
                eq("course", course)
                order("startDate", "asc")
            } as List<Quiz>

            DateFormat fn = new SimpleDateFormat("MM-dd-yy")
            NumberFormat nf = new DecimalFormat("#0.00")

            def (min, max) = quizList.size() > 0 ?
                    [fn.format(quizList.first().startDate), fn.format(quizList.last().startDate)] : ["", ""]

            Set<User> students = course.students
            response.setHeader("Content-disposition", "filename=grades-${course.name}_${min}___${max}.csv")
            response.contentType = "text/csv"
            response.characterEncoding = "UTF-8"

            outputStream << "Name"
            outputStream << ",Email"
            quizList.each { Quiz quiz ->
                outputStream << ",${quiz.name}"
            }
            outputStream << ",Average"
            outputStream << "\n"
            outputStream.flush()

            students.eachWithIndex { User student, int i ->
                outputStream << (student.firstName ? "${student.firstName} ${student.lastName}" : "Not Specified")
                outputStream << ",${student.email}"

                def total = 0

                quizList.each { Quiz quiz ->
                    Grade grade = Grade.findByStudentAndQuiz(student, quiz)
                    if (grade) {
                        outputStream << ",${nf.format(grade.grade)}"
                        total += grade.grade
                    } else
                        outputStream << ",N/A"
                }

                outputStream << (quizList.size() != 0 ? ",${nf.format(total / quizList.size())}" : "N/A")

                if (i < students.size() - 1) {
                    outputStream << "\n"
                }
                outputStream.flush()
            }
        } else {
            result.success = false
            result.errorCode = HttpStatus.UNAUTHORIZED.value()
            result.message = HttpStatus.UNAUTHORIZED.reasonPhrase
        }
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

    private boolean quizSubmissionExists(Quiz quiz, User user) {
        def quizSubmission = QuizSubmission.where {
            quiz == quiz && student == user
        }.find()

        return quizSubmission != null
    }

    private boolean quizIsOpen(Quiz quiz) {
        def now = new Date()
        now >= quiz.startDate && now < quiz.endDate
    }

    private Date parseTimestamp(String unixTime) {
        if (unixTime.isLong()) {
            return new Date(unixTime.toLong())
        }
        return null
    }

    private List<Boolean> toBooleanList(String commaSeparatedStr) {
        List<Boolean> boolList = new ArrayList<>()
        def stringList = commaSeparatedStrToList(commaSeparatedStr)
        stringList.forEach { s ->
            if (s == "false") boolList.add(false)
            else if (s == "true") boolList.add(true)
            else return null
        }
        return boolList
    }

    private List<String> commaSeparatedStrToList(String str) {
        str.indexOf(",") != -1 ? str.split(",").toList() : [str]
    }

}
