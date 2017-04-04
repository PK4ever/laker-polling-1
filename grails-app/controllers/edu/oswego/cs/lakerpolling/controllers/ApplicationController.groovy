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
                // render(view: 'dashboardStudent')
                render(view: 'dashboardInstructor')
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
            session.setAttribute("courseId", courseId)
            render(view: 'courseLandingInstructor')
        }
    }


    private QueryResult<AuthToken> hasAccess() {
        String access = session.getAttribute("access")
        preconditionService.accessToken(access)
    }

}
