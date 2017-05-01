package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.BootStrapSpec
import grails.core.GrailsApplication
import grails.plugins.rest.client.RestResponse
import org.springframework.beans.factory.annotation.Autowired

class QuizControllerSpec extends BootStrapSpec {

    @Autowired
    GrailsApplication grailsApplication

    void "Test postQuiz(): 1 - Instructor"() {
        Map<String, Object> params = new HashMap<>()
        params.put("name","Quiz1")
        params.put("start_timestamp", "1508544000")
        params.put("end_timestamp", "1508630400")
        testWith(VALID_INSTRUCTOR, VALID_COURSE, params)
        params.put("course_id", VALID_COURSE.id.toString())
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        given:
        RestResponse response = post("/api/quiz", params)

        when: "PostQuiz Is Ran"

        then: "The Output Should Be The Following"
        println response.json
        "a".equalsIgnoreCase("b")
    }
}
