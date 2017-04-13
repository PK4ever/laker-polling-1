package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.Role
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.Pair
import edu.oswego.cs.lakerpolling.util.QueryResult
import edu.oswego.cs.lakerpolling.util.RoleType
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

@Transactional
class UserService {

    class UserErrors {
        final static String INVALID_ACCESS_TOKEN = "Access token invalid"
        final static String USER_NOT_FOUND = "User not found with given token"
    }

    /**
     * Checks if the given user is an Instructor
     * @param user - The user to check
     * @return A boolean representing whether the user is an Instructor
     */
    boolean checkIfInstructor(User user) { user.role.type == RoleType.INSTRUCTOR }

    /**
     * Gets the user with the specified access token
     * @param token - the access token for the user
     * @return The user associated with access token
     */
    QueryResult<User> getUser(String token) {
        QueryResult queryResult = new QueryResult()
        queryResult.success = false
        AuthToken authToken = AuthToken.findByAccessToken(token)
        if (authToken == null) {
            queryResult.message = UserErrors.INVALID_ACCESS_TOKEN
            queryResult.errorCode = HttpStatus.BAD_REQUEST.value()
            return queryResult
        }

        User user = authToken.user

        if (user == null) {
            queryResult.message = UserErrors.USER_NOT_FOUND
            queryResult.errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value()
            return queryResult
        }
        queryResult.data = user
        queryResult.success = true
        return queryResult
    }

    /**
     * Gets the user associated with the given email or, if no such user exists, creates a placeholder user account for
     * that email. This placeholder account will allow instructors to add students to courses before the students have
     * registered with the system.
     * @param email - The email of the user
     * @return A User object
     */
    User getOrMakeByEmail(String email) {
        User user = User.findByEmail(email)

        if (user == null) {
            user = new User(email: email)
            user.setRole(new Role(type: RoleType.STUDENT))
            user.save(flush: true, failOnError: true)
        }

        return user
    }

    /**
     * Method to get, update or insert a user from the given payload.
     * @param payload - The payload to grab data from.
     * @return A pair containing a User and their associated AuthToken should all operations go successfully.
     */
    Optional<Pair<User, AuthToken>> getMakeOrUpdate(String subj, String first, String last,
                                                    String imageUrl, String email) {
        User user
        AuthToken token

        token = AuthToken.findBySubject(subj)

        // we have found an auth object. Therefore we have this user and they've signed in before.
        if (token != null) {
            user = token.user
            if (user.email != email) {
                user.email = email
            }
            token.accessToken = UUID.randomUUID()
            token.save(flush: true, failOnError: true)
        } else {
            // two possible situations here. This is a new user or it's a pre-loaded user
            //signing in for the first time.

            user = User.findByEmail(email)
            if (user == null) { //it's a new user
                user = new User(firstName: first, lastName: last, imageUrl: imageUrl, email: email)
                user.setRole(new Role(type: RoleType.STUDENT))
            } else {
                //we've found the pre-loaded user, set their values to the ones active the g profile
                user.firstName = first
                user.lastName = last
                user.imageUrl = imageUrl
            }
        }

        user = user.save(flush: true, failOnError: true)
        if (user.authToken == null) {
            user.setAuthToken(new AuthToken(subject: subj, accessToken: UUID.randomUUID()))
            user = user.save(flush: true, failOnError: true)
        }

        token = user.authToken

        user != null ? Optional.of(new Pair<User, AuthToken>(user, token))
                : Optional.empty()

    }

    QueryResult<List<User>> findUsersBy(AuthToken token, String first, String last, String email) {
        QueryResult<List<User>> result

        if (checkIfInstructor(token.user)) {
            def users = User.createCriteria().list {
                if (first) {
                    like('firstName', first.concat("%"))
                }

                if (last) {
                    like('lastName', last.concat("%"))
                }

                if (email) {
                    eq('email', email.concat("%"))
                }
            } as List<User>

            result = new QueryResult<>(success: true, data: users)
        } else {
            result = QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
        }

        result
    }

    QueryResult<User> createUser(AuthToken token, String email, String role) {
        QueryResult<User> result
        if (checkIfInstructor(token.user)) {
            if (User.findByEmail(email) == null) {
                if (email.contains("oswego.edu")) {
                    RoleType roleType

                    if (role != null) {
                        try {
                            roleType = role as RoleType
                        } catch (IllegalArgumentException e) {
                            return new QueryResult<>(success: false, errorCode: HttpStatus.BAD_REQUEST.value(), message: "Unexpected role:" + role)
                        }
                    } else {
                        roleType = RoleType.STUDENT
                    }

                    User temp = new User(email: email)
                    temp.setRole(new Role(type: roleType))
                    temp.save(flush: true)
                    result = new QueryResult<>(success: true, data: temp)
                } else {
                    result = new QueryResult<>(success: false, errorCode: HttpStatus.BAD_REQUEST.value(), message: "Non oswego.edu email.")
                }
            } else {
                result = new QueryResult<>(success: false, errorCode: HttpStatus.BAD_REQUEST.value(), message: "Email already exists")
            }
        } else {
            result = QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
        }
        result
    }

}
