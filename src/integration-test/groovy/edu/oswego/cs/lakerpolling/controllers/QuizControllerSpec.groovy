package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.BootStrapSpec

class QuizControllerSpec extends BootStrapSpec {

    void "Test postQuiz(): 1 - Valid Instructor"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("name","Quiz1")
        params.put("start_timestamp", "1508544000")
        params.put("end_timestamp", "1508630400")
        testWith(VALID_INSTRUCTOR, VALID_COURSE, params)
        params.put("course_id", VALID_COURSE.id.toString())
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "PostQuiz Is Queried"
        def response = post("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
    }

    void "Test postQuiz(): 2 - Valid Admin"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("name","Quiz1")
        params.put("start_timestamp", "1508544000")
        params.put("end_timestamp", "1508630400")
        testWith(VALID_ADMIN, VALID_COURSE, params)
        params.put("course_id", VALID_COURSE.id.toString())
        params.put("access_token", VALID_ADMIN.authToken.accessToken)

        when: "PostQuiz Is Queried"
        def response = post("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
    }

    void "Test postQuiz(): 3 - Valid Student"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("name","Quiz1")
        params.put("start_timestamp", "1508544000")
        params.put("end_timestamp", "1508630400")
        testWith(VALID_STUDENT, VALID_COURSE, params)
        params.put("course_id", VALID_COURSE.id.toString())
        params.put("access_token", VALID_STUDENT.authToken.accessToken)

        when: "PostQuiz Is Queried"
        def response = post("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 403
        response.json.status == "failure"
    }

    void "Test postQuiz(): 4 - Invalid Instructor"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("name","Quiz1")
        params.put("start_timestamp", "1508544000")
        params.put("end_timestamp", "1508630400")
        testWith(INVALID_INSTRUCTOR, VALID_COURSE, params)
        params.put("course_id", VALID_COURSE.id.toString())
        params.put("access_token", INVALID_INSTRUCTOR.authToken.accessToken)

        when: "PostQuiz Is Queried"
        def response = post("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 401
        response.json.status == "failure"
    }

    void "Test postQuiz(): 5 - Invalid Course"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("name","Quiz1")
        params.put("start_timestamp", "1508544000")
        params.put("end_timestamp", "1508630400")
        testWith(VALID_INSTRUCTOR, INVALID_COURSE, params)
        params.put("course_id", "45")
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "PostQuiz Is Queried"
        def response = post("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 400
        response.json.status == "failure"
    }

    void "Test postQuiz(): 6 - Invalid Start End Time"() {
        given: "The Following Parameters"
        Map<String, Object> params = new HashMap<>()
        params.put("name","Quiz1")
        params.put("end_timestamp", "1508544000")
        params.put("start_timestamp", "1508630400")
        testWith(VALID_INSTRUCTOR, VALID_COURSE, params)
        params.put("course_id", VALID_COURSE.id.toString())
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "PostQuiz Is Queried"
        def response = post("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 400
        response.json.status == "failure"
    }
}
