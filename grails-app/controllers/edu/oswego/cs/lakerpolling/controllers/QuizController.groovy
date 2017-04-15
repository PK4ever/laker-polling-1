package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.services.PreconditionService
import edu.oswego.cs.lakerpolling.services.QuizService
import edu.oswego.cs.lakerpolling.util.QueryResult

class QuizController {

    PreconditionService preconditionService
    QuizService quizService

    /**
     *
     * @param access_token - the access token of the user
     * @param course_id - the id of the course that this particular quiz with relate to
     * @param name -(optional) the name of the quiz
     * @param start_time - The starting date students will be able to access this quiz
     * @param endTime - The ending date that the students will be able to access this quiz.
     * @return
     */
    def postQuiz(String access_token, String course_id, String name, String start_time, String end_time) {
        def result = preconditionService.notNull(params, ["access_token", "course_id", "start_time", "endTime"])
        def token = preconditionService.accessToken(access_token).data

        if(result.success) {
            def newQuiz = quizService.createQuiz(token, course_id, name, start_time, end_time)
            if(newQuiz) {
                render(view: 'create', model: [quiz: newQuiz, token: token])
            } else {
                render(view: '../failure', model: [errorCode: 400, message: "Could not create Quiz"])
            }
        } else {
            render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
        }

    }

    /**
     * Endpoint to get a list of all of the question IDs for a quiz.
     * @param access_token - The access token of the requesting user
     * @param quiz_id - the id of the quiz
     */
    def getQuizQuestions(String access_token, String quiz_id) {
        def require = preconditionService.notNull(params, ["access_token", "quiz_id"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            def result = quizService.getQuizQuestions(token, quiz_id)
            if (result.success) {
                render(view: 'questionIdList', model: [token: token, quizId: quiz_id.toLong(), questionIds: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     * Endpoint to add a question to an existing quiz.
     * @param access_token - The access token of the requesting user
     * @param quiz_id - the id of the quiz
     * @param text - the text of the question
     * @param choices - a comma-separated String containing the text of the answer options
     * @param answers - a comma-separated String of Boolean values representing whether each option in the question is a correct choice or not.
     */
    def postQuestionToQuiz(String access_token, String quiz_id, String text, String choices, String answers) {
        def require = preconditionService.notNull(params, ["access_token", "quiz_id", "text", "choices", "answers"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            def result = quizService.postQuestionToQuiz(token, quiz_id, text, choices, answers)
            if (result.success) {
                render(view: 'quizQuestion', model: [token: token, question: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }
}
