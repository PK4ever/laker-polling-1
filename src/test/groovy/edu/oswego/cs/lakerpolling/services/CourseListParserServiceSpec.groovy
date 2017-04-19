package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.util.QueryResult
import grails.test.mixin.TestFor
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

@TestFor(CourseListParserService)
class CourseListParserServiceSpec extends Specification {

    def "Test Email With Header"() {
        when: "The CSV is loaded"
        QueryResult<List<String>> users = service.parse(getFile("EmailWithHeader.csv"))

        then: "There should be the following users"
        users.data.contains("user1@oswego.edu")
        users.data.contains("user2@oswego.edu")
        users.data.contains("user3@oswego.edu")
    }

    def "Test Email Without Header"() {
        when: "The CSV is loaded"
        QueryResult<List<String>> users = service.parse(getFile("EmailWithoutHeader.csv"))

        then: "There should be the following users"
        users.data.contains("user1@oswego.edu")
        users.data.contains("user2@oswego.edu")
        users.data.contains("user3@oswego.edu")
    }

    def "Test Username With Header"() {
        when: "The CSV is loaded"
        QueryResult<List<String>> users = service.parse(getFile("UsernameWithHeader.csv"))

        then: "There should be the following users"
        users.data.contains("user1@oswego.edu")
        users.data.contains("user2@oswego.edu")
        users.data.contains("user3@oswego.edu")
    }

    def "Test Username Without Header"() {
        when: "The CSV is loaded"
        QueryResult<List<String>> users = service.parse(getFile("UsernameWithoutHeader.csv"))

        then: "There should be no users"
        !users.success
    }

    def "Test Different Delimiter"() {
        when: "The CSV is loaded"
        QueryResult<List<String>> users = service.parse(getFile("DifferentDelimiter.csv"))

        then: "There should be no users"
        !users.success
    }

    private MockMultipartFile getFile(String name) {
        return new MockMultipartFile(name,this.getClass().getClassLoader().getResourceAsStream("courseListParserServiceSpec/$name"))
    }
}
