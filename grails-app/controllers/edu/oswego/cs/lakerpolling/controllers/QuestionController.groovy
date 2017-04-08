package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.domains.Question
import edu.oswego.cs.lakerpolling.services.PreconditionService
import edu.oswego.cs.lakerpolling.services.QuestionService

class QuestionController {
	static responseFormats = ['json', 'xml']
    PreconditionService preconditionService
    QuestionService questionService

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

    def getAnswers(String access_token, String question_id) {
        def result = preconditionService.notNull(params, ["access_token",  "question_id"])
        def token = preconditionService.accessToken(access_token).data

        if(result.success) {
            def question = Question.findById(question_id.toLong())
            if(question) {
                if(!question.active) render(view: 'getAnswer', model: [question: question, token: token])
                else render(view: '../failure', model: [errorCode: 400, message: "question still enabled"])
            } else {
                render(view: '../failure', model: [errorCode: 400, message: "could not find question!"])
            }
        } else {f
            render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
        }
    }

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
}
