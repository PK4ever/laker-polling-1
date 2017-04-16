package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.Role
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.QueryResult
import edu.oswego.cs.lakerpolling.util.RoleType
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

@Transactional
class RoleService {


    QueryResult<Role> getUserRole(AuthToken token) {
        User user = token.user
        new QueryResult<Role>(success: true, data: user.role)
    }

    QueryResult<Role> getUserRole(AuthToken token, long userId) {
        RoleType requestingRole = token.user.role.type
        if (requestingRole != RoleType.INSTRUCTOR && requestingRole != RoleType.ADMIN) {
            return QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
        }

        User user = User.findById(userId)
        QueryResult<Role> result

        if (user != null) {
            result = new QueryResult<>(success: true, data: user.role)
        } else {
            result = new QueryResult<>(success: false, message: "No user with user_id:$userId exists.")
        }

        result
    }

    QueryResult<Role> updateCurrent(AuthToken token, String current) {
        doUpdateCurrent(token.user, current)
    }

    QueryResult<Role> updateMaster(AuthToken token, long userId, String master) {
        RoleType requestingRole = token.user.role.type
        if (requestingRole != RoleType.INSTRUCTOR && requestingRole != RoleType.ADMIN) {
            return QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
        }

        Optional<RoleType> optional = stringToRoleType(master)
        if (!optional.isPresent()) {
            return new QueryResult<Role>(success: false, message: "Role:$master does not exist")
        }

        User user = User.findById(userId)
        QueryResult<Role> result

        if (user != null) {
            Role role = user.role
            role.master = optional.get()

            Role temp = role.save(flush: true, failOnError: true)
            result = new QueryResult<>(success: true, data: temp)
        } else {
            result = new QueryResult<>(success: false, message: "No user with user_id:$userId exists.")
        }

        result
    }

    private QueryResult<Role> doUpdateCurrent(User user, String current) {
        Optional<RoleType> optional = stringToRoleType(current)
        if (optional.isPresent()) {
            doUpdateCurrent(user, optional.get())
        } else {
            new QueryResult<Role>(success: false, message: "Role:$current does not exist")
        }
    }

    private QueryResult<Role> doUpdateCurrent(User user, RoleType update) {
        QueryResult<Role> result

        Role role = user.role
        RoleType master = role.master
        if (roleLessOrEq(update, master)) {
            role.type = update
            Role temp = role.save(flush: true, failOnError: true)
            result = new QueryResult<>(success: true, data: temp)
        } else {
            result = QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
            result.message = "Role:${update.toString()} should be less than ${master.toString()}"
        }

        result
    }

    private boolean roleLessOrEq(RoleType a, RoleType b) {
        if (a == b) {
            true
        } else {
            if (b == RoleType.ADMIN) {
                a == RoleType.INSTRUCTOR || a == RoleType.STUDENT
            } else {
                a == RoleType.STUDENT
            }
        }
    }

    private Optional<RoleType> stringToRoleType(String inputValue) {
        try {
            Optional.of(inputValue as RoleType)
        } catch (IllegalArgumentException e) {
            Optional.empty()
        }
    }

}
