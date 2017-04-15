package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.services.PreconditionService
import edu.oswego.cs.lakerpolling.services.UserService
import edu.oswego.cs.lakerpolling.util.QueryResult
import org.springframework.http.HttpStatus

class UserController {

    static responseFormats = ['json', 'xml']

    PreconditionService preconditionService
    UserService userService

    def getUser(String access_token, String first_name, String last_name, String email) {
        QueryResult<AuthToken> checks = new QueryResult<>()
        preconditionService.notNull(params, ['access_token'], checks)
        preconditionService.accessToken(access_token, checks)

        if (checks.success) {
            if (first_name || last_name || email) {
                QueryResult<List<User>> queryResult = userService.findUsersBy(checks.data, first_name, last_name, email)
                if (queryResult.success) {
                    render(view: 'users', model: [token: checks.data, users: queryResult.data])
                } else {
                    render(view: '../failure', model: [errorCode: queryResult.errorCode, message: queryResult.message])
                }
            } else {
                render(view: '../failure', model: [errorCode: HttpStatus.BAD_REQUEST.value(),
                                                   message  : 'Must specify at least one search parameter'])
            }
        } else {
            render(view: '../failure', model: [errorCode: checks.errorCode, message: checks.message])
        }

    }

    def postUser(String access_token, String email, String role) {
        QueryResult<AuthToken> checks = new QueryResult<>()
        preconditionService.notNull(params, ['access_token', 'email'], checks)
        preconditionService.accessToken(access_token, checks)

        if (checks.success) {
            QueryResult<User> result = userService.createUser(checks.data, email, role)
            if (result.success) {
                render(view: 'users', model: [token: checks.data, users: [result.data]])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: checks.errorCode, message: checks.message])
        }
    }

}
