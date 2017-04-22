package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.domains.Question
import edu.oswego.cs.lakerpolling.services.PreconditionService
import edu.oswego.cs.lakerpolling.services.QuestionService

class QuestionController {
	static responseFormats = ['json', 'xml']
    PreconditionService preconditionService
    QuestionService questionService

    /**
     * creates a new question
     * @param access_token - the access_token of the user
     * @param course_id - the course id of the selected course
     * @param question - (optional) the actual string representing the question
     * @param answers - a list of answers represented as booleans
     * @return - returns a json view
     */
    def createQuestion(String access_token, String course_id, String question, String answers) {

        def result = preconditionService.notNull(params, ["access_token", "answers" , "course_id"])
        def token = preconditionService.accessToken(access_token).data

        if(result.success) {
            def newQuestion = questionService.createQuestion(token, question, course_id, answers)
            if(newQuestion) {
                render(view: 'create', model: [question: newQuestion, token: token])
            } else {
                render(view: '../failure', model: [errorCode: 400, message: "could not find course!"])
            }
        } else {
            render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
        }
    }

    /**
     * gets a collection of integers that represents the number of people who answered each question
     * @param access_token - the access_token of the user
     * @param question_id - the id of the question
     * @return - returns a json view
     */
    def getAnswers(String access_token, String question_id) {
        def require = preconditionService.notNull(params, ["access_token",  "question_id"])
        def token = preconditionService.accessToken(access_token).data

        if(require.success) {
            def result = questionService.getAnswers(token, question_id)
            if(result.success) {
                render(view: 'getAnswer', model: [answers: result.data, questionId: question_id.toLong(), token: token])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     * answers a selected question
     * @param access_token - the access_token of the user
     * @param question_id - the id of the question
     * @param answer - the collection of answers represented as a list of booleans
     * @return - returns a json view
     */
    def answerQuestion(String access_token, String question_id, String answer) {
        def result = preconditionService.notNull(params, ["access_token", "question_id", "answer"])
        def token = preconditionService.accessToken(access_token).data
        if(result.success) {
            if(questionService.answerQuestion(token, question_id, answer)) {
                render(view: 'answerQuestion', model: [token: token])
            } else {
                render(view: '../failure', model: [errorCode: 400, message: "could not answer question"])
            }
        } else {
            render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
        }
    }

    /**
     * change a question from deactivated to active or vice versa
     * @param access_token - the access_token of the user
     * @param question_id - the id of the selected question
     * @param flip - the boolean representing active or inactive
     * @return - returns a json view
     */
    def changeQuestionStatus(String access_token, String question_id, boolean flip) {
        def result = preconditionService.notNull(params, ["access_token", "question_id"])
        def token = preconditionService.accessToken(access_token).data

        if(result.success) {
            if(questionService.flipQuestion(token, question_id, flip)) {
                render(view: 'answerQuestion', model: [token: token])
            } else {
                render(view: '../failure', model: [errorCode: 400, message: "something couldn't be found"])
            }
        }
    }

    /**
     * gets all questions for a selected course
     * @param access_token - the access_token of the user
     * @param course_id - the id of the selected course
     * @return - returns a json view
     */
    def getQuestion(String access_token, String course_id) {
        def result = preconditionService.notNull(params, ["access_token", "course_id"])
        def token = preconditionService.accessToken(access_token).data

        if(result.success) {
            def question = questionService.getQuestion(token, course_id)
            if(question) {
                def ids = question.collect{q -> q.id}
                render(view: 'getQuestion', model:[token: token, questionId: ids])
            } else render(view: '../failure', model: [errorCode: 400, message: "no questions"])
        } else render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
    }

    /**
     * gets the active questions for a selected course
     * @param access_token - the access_token of the user
     * @param course_id - the id of the selected course
     * @return - returns a json view
     */
    def getActiveQuestion(String access_token, String course_id) {
        def result = preconditionService.notNull(params, ["access_token", "course_id"])
        def token = preconditionService.accessToken(access_token).data

        if(result.success) {
            def question = questionService.getActiveQuestion(token, course_id)
            if(question) {
                render(view: 'activeQuestion', model: [token: token, id: question.id])
            } else render(view: '../failure', model: [errorCode: 400, message: "no available questions"])
        } else render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])

    }

    def getQuestionResults(String access_token, String date, String course_id) {
        def require = preconditionService.notNull(params, ["access_token", "date", "course_id"])
        def token = preconditionService.accessToken(access_token).data

        if(require.success) {
            def result = questionService.getResults(token, date, course_id)
            if(result.success) {
                render(view: 'getQuestionAnswers', model: [token: token, allQuestions: result.data])
            } else {
                render(view: '../failure', model: [message: result.message, errorCode: result.errorCode])
            }
        } else {
            render(view: '../failure', model: [message: require.message, errorCode: require.errorCode])
        }
    }
}
