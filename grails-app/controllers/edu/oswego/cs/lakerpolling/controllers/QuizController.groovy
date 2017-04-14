package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.services.PreconditionService
import edu.oswego.cs.lakerpolling.services.QuizService
import edu.oswego.cs.lakerpolling.util.QueryResult

class QuizController {

    PreconditionService preconditionService
    QuizService quizService

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
