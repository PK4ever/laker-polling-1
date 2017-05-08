package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.BootStrapSpec
import edu.oswego.cs.lakerpolling.util.RoleType

/**
 * Created by keithmartin on 5/5/17.
 */
class UserControllerSpec extends BootStrapSpec {

    void "Test getUser(): 1 - Instructor"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("first_name", VALID_INSTRUCTOR.firstName)
        params.put("last_name", VALID_INSTRUCTOR.lastName)
        params.put("email", VALID_INSTRUCTOR.email)
        testWith(VALID_INSTRUCTOR, params)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "GetUser Is Queried"
        def response = get("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 200
    }

    void "Test getUser(): 2 - Admin"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("first_name", VALID_ADMIN.firstName)
        params.put("last_name", VALID_ADMIN.lastName)
        params.put("email", VALID_ADMIN.email)
        testWith(VALID_ADMIN, params)
        params.put("access_token", VALID_ADMIN.authToken.accessToken)

        when: "GetUser Is Queried"
        def response = get("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 200
    }

    void "Test getUser(): 3 - Student"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("first_name", VALID_STUDENT.firstName)
        params.put("last_name", VALID_STUDENT.lastName)
        params.put("email", VALID_STUDENT.email)
        testWith(VALID_STUDENT, params)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "GetUser Is Queried"
        def response = get("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 200
    }

    void "Test getUser(): 4 - All invalid eq classes"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("first_name", INVALID_ADMIN.firstName)
        params.put("last_name", INVALID_ADMIN.lastName)
        params.put("email", INVALID_ADMIN.email)
        testWith(INVALID_ADMIN, params)
        params.put("access_token", INVALID_ADMIN.authToken.accessToken)

        when: "GetUser Is Queried"
        def response = get("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 401
    }

    void "Test postUser(): 1 - Instructor"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("email", VALID_INSTRUCTOR.email)
        params.put("role", RoleType.STUDENT)
        testWith(VALID_INSTRUCTOR, params)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "PostUser Is Queried"
        def response = post("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 200
    }

    void "Test postUser(): 2 - Admin"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("email", VALID_ADMIN.email)
        params.put("role", VALID_ADMIN.role)
        testWith(VALID_ADMIN, params)
        params.put("access_token", VALID_ADMIN.authToken.accessToken)

        when: "PostUser Is Queried"
        def response = post("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 200
    }

    void "Test postUser(): 3 - Student"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("email", VALID_STUDENT.email)
        params.put("role", RoleType.STUDENT)
        testWith(VALID_STUDENT, params)
        params.put("access_token", VALID_STUDENT.authToken.accessToken)

        when: "PostUser Is Queried"
        def response = post("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 401
    }

    void "Test postUser(): 4 - All invalid eq classes"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("email", INVALID_INSTRUCTOR.email)
        params.put("role", INVALID_INSTRUCTOR.role)
        testWith(INVALID_INSTRUCTOR, params)
        params.put("access_token", INVALID_INSTRUCTOR.authToken.accessToken)

        when: "PostUser Is Queried"
        def response = post("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 401
    }

    void "Test getUserRole(): 1 - Instructor"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("user_id", VALID_INSTRUCTOR.id)
        testWith(VALID_INSTRUCTOR, params)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "GetUserRole Is Queried"
        def response = get("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 200
    }

    void "Test getUserRole(): 2 - Admin"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("user_id", VALID_ADMIN.id)
        testWith(VALID_ADMIN, params)
        params.put("access_token", VALID_ADMIN.authToken.accessToken)

        when: "GetUserRole Is Queried"
        def response = get("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 200
    }

    void "Test getUserRole(): 3 - Student"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("user_id", VALID_STUDENT.id)
        testWith(VALID_STUDENT, params)
        params.put("access_token", VALID_STUDENT.authToken.accessToken)

        when: "GetUserRole Is Queried"
        def response = get("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 200
    }

    void "Test getUserRole(): 4 - All invalid eq classes"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("email", INVALID_INSTRUCTOR.email)
        params.put("role", INVALID_INSTRUCTOR.role)
        testWith(INVALID_INSTRUCTOR, params)
        params.put("access_token", INVALID_INSTRUCTOR.authToken.accessToken)

        when: "GetUserRole Is Queried"
        def response = post("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 401
    }

    void "Test putUserRole(): 1 - Instructor"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("current", VALID_INSTRUCTOR.role)
        params.put("master", VALID_INSTRUCTOR.role)
        params.put("email", VALID_INSTRUCTOR.email)
        testWith(VALID_INSTRUCTOR, params)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "PutUserRole Is Queried"
        def response = put("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 200
    }

    void "Test putUserRole(): 2 - Admin"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("current", VALID_ADMIN.role)
        params.put("master", VALID_ADMIN.role)
        params.put("email", VALID_ADMIN.email)
        testWith(VALID_ADMIN, params)
        params.put("access_token", VALID_ADMIN.authToken.accessToken)

        when: "PutUserRole Is Queried"
        def response = put("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 200
    }

    void "Test putUserRole(): 3 - Student"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("current", VALID_STUDENT.role)
        params.put("master", VALID_STUDENT.role)
        params.put("email", VALID_STUDENT.email)
        testWith(VALID_STUDENT, params)
        params.put("access_token", VALID_STUDENT.authToken.accessToken)

        when: "PutUserRole Is Queried"
        def response = put("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 200
    }

    void "Test putUserRole(): 4 - All invalid eq classes"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("current", INVALID_INSTRUCTOR.role)
        params.put("master", INVALID_INSTRUCTOR.role)
        params.put("email", INVALID_INSTRUCTOR.email)
        testWith(INVALID_INSTRUCTOR, params)
        params.put("access_token", INVALID_INSTRUCTOR.authToken.accessToken)

        when: "PutUserRole Is Queried"
        def response = put("/api/user", params)

        then: "The Output Should Be The Following"
        response.status == 401
    }

}
