package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.services.PreconditionService
import edu.oswego.cs.lakerpolling.util.QueryResult
import edu.oswego.cs.lakerpolling.util.RoleType

class ApplicationController {

    PreconditionService preconditionService

    def landing() {
        render(view: 'landing')
    }

    def dashboard() {
        QueryResult<AuthToken> require = hasAccess()
        if (require.success) {
            User user = require.data.user
            RoleType type = user.role.type
            if (type == RoleType.STUDENT) {
                render(view: 'dashboardStudent')
            } else if (type == RoleType.INSTRUCTOR) {
                render(view: 'dashboardInstructor')
            } else if (type == RoleType.ADMIN) {
                render(view: 'dashboardAdmin')
            } else {
                session.invalidate()
                redirect(controller: 'application', action: 'landing')
            }
        } else {
            session.invalidate()
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def courseView(long courseId) {
        QueryResult<AuthToken> require = hasAccess()
        if(require.success) {
            User user = require.data.user
            RoleType type = user.role.type
            def preReq = preconditionService.notNull(params, ["courseId"])
            if(preReq.success) {
                session.setAttribute("courseId", courseId)
                if (type == RoleType.STUDENT) {
                    render(view: 'courseLandingStudent')
                } else if (type == RoleType.INSTRUCTOR) {
                    render(view: 'courseLandingInstructor')
                }
            } else {
                render(view: '../failure', model: [errorCode: preReq.errorCode, message: preReq.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def classRoster() {
        def require = hasAccess()
        if(require.success) {
            render(view: 'classRoster')
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }


    def createQuestionView(long courseId) {
        QueryResult<AuthToken> require = hasAccess()
        if(require.success) {
            def preReq = preconditionService.notNull(params, ["courseId"])
            if(preReq.success) {
//                session.setAttribute("courseId", courseId)
                render(view: 'instructorQuestionBuilder')
            } else {
                render(view: '../failure', model: [errorCode: preReq.errorCode, message: preReq.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def resultsView(long courseId) {
        QueryResult<AuthToken> require = hasAccess()
        if(require.success) {
            def preReq = preconditionService.notNull(params, ["courseId"])
            if(preReq.success) {
                session.setAttribute("courseId", courseId)
                render(view: 'pollResults')
            } else {
                render(view: '../failure', model: [errorCode: preReq.errorCode, message: preReq.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def answerView(long courseId) {
        QueryResult<AuthToken> require = hasAccess()
        if(require.success) {
            def preReq = preconditionService.notNull(params, ["courseId"])
            if(preReq.success) {
                session.setAttribute("courseId", courseId)
                render(view: 'studentQuestionResponse')
            } else {
                render(view: '../failure', model: [errorCode: preReq.errorCode, message: preReq.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    private QueryResult<AuthToken> hasAccess() {
        String access = session.getAttribute("access")
        preconditionService.accessToken(access)
    }

}
