package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.services.PreconditionService
import edu.oswego.cs.lakerpolling.services.QuizService

class QuizController {

    PreconditionService preconditionService
    QuizService quizService

    /**
     * Endpoint to get a list of all of the question IDs for a quiz.
     * @param access_token - The access token of the requesting user
     * @param quiz_id - the id of the quiz
     */
    def getQuizzes(String access_token, String course_id, String quiz_id) {
        def require = preconditionService.notNull(params, ["access_token"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            if (quiz_id) {
                def result = quizService.getQuiz(token, quiz_id)
                if (result.success) {
                    def quiz = result.data
                    render(view: 'getQuiz', model: [token: token, courseID: quiz.course.id, quiz: quiz])
                } else {
                    render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
                }
            }
            else if (course_id){
                def result = quizService.getAllQuizzes(token, course_id)
                if (result.success) {
                    render(view: 'getQuizzes', model: [token: token, courseID: course_id.toLong(), quizzes: result.data])
                } else {
                    render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
                }
            }
            else {
                render(view: '../failure', model: [errorCode: 400, message: "Missing quiz_id or question_id parameter"])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     * Endpoint to get a list of all of the question IDs for a quiz.
     * @param access_token - The access token of the requesting user
     * @param quiz_id - the id of the quiz
     */
    def getQuizQuestions(String access_token, String quiz_id, String question_id) {
        def require = preconditionService.notNull(params, ["access_token", "quiz_id"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            if (question_id) {
                def result = quizService.getQuestion(token, quiz_id, question_id)
                if (result.success) {
                    render(view: 'quizQuestion', model: [token: token, question: result.data])
                } else {
                    render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
                }
            }
            else {
                def result = quizService.getAllQuestionIds(token, quiz_id)
                if (result.success) {
                    render(view: 'questionIdList', model: [token: token, quizId: quiz_id.toLong(), questionIds: result.data])
                } else {
                    render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
                }
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

    /**
     * Removes the given question.
     * @param access_token - the access_token of the user
     * @param quiz_id - the id of the quiz
     * @param question_id - the id of the question to remove
     * @return - returns a json view
     */
    def deleteQuestion(String access_token, String quiz_id, String question_id) {
        def require = preconditionService.notNull(params, ["access_token", "question_id"])
        def token = preconditionService.accessToken(access_token).data

        if(require.success) {
            def result = quizService.deleteQuestion(token, quiz_id, question_id)
            if(result.success) {
                render(view: 'deleteQuestion', model: [token: token])
            } else render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
        } else render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
    }
}
