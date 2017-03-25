package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.services.PreconditionService
import edu.oswego.cs.lakerpolling.util.QueryResult
import edu.oswego.cs.lakerpolling.util.RoleType

class ApplicationController {

    PreconditionService preconditionService

    def landing() {

    }

    def dashboard() {
        String access = session.getAttribute("access")
        QueryResult<AuthToken> require = preconditionService.accessToken(access)
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

}
