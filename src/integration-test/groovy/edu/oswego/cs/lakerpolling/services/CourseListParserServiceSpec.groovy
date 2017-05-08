package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.BootStrapSpec
import edu.oswego.cs.lakerpolling.util.QueryResult
import grails.test.mixin.TestFor
import org.springframework.mock.web.MockMultipartFile

@TestFor(CourseListParserService)
class CourseListParserServiceSpec extends BootStrapSpec {

    def "test parse(): Email With Header"() {
        testWith()
        when: "The CSV is loaded"
        println "Testing With File: EmailWithHeader.csv"
        QueryResult<List<String>> users = service.parse(getFile("EmailWithHeader.csv"))

        then: "There should be the following users"
        users.data.contains("user1@oswego.edu")
        users.data.contains("user2@oswego.edu")
        users.data.contains("user3@oswego.edu")
    }

    def "test parse(): Email Without Header"() {
        testWith()
        when: "The CSV is loaded"
        println "Testing With File: EmailWithoutHeader.csv"
        QueryResult<List<String>> users = service.parse(getFile("EmailWithoutHeader.csv"))

        then: "There should be the following users"
        users.data.contains("user1@oswego.edu")
        users.data.contains("user2@oswego.edu")
        users.data.contains("user3@oswego.edu")
    }

    def "test parse(): Username With Header"() {
        testWith()
        when: "The CSV is loaded"
        println "Testing With File: UsernameWithHeader.csv"
        QueryResult<List<String>> users = service.parse(getFile("UsernameWithHeader.csv"))

        then: "There should be the following users"
        users.data.contains("user1@oswego.edu")
        users.data.contains("user2@oswego.edu")
        users.data.contains("user3@oswego.edu")
    }

    def "test parse(): Username Without Header"() {
        testWith()
        when: "The CSV is loaded"
        println "Testing With File: UsernameWithoutHeader.csv"
        QueryResult<List<String>> users = service.parse(getFile("UsernameWithoutHeader.csv"))

        then: "There should be no users"
        !users.success
    }

    def "test parse(): Different Delimiter"() {
        testWith()
        when: "The CSV is loaded"
        println "Testing With File: DifferentDelimiter.csv"
        QueryResult<List<String>> users = service.parse(getFile("DifferentDelimiter.csv"))

        then: "There should be no users"
        !users.success
    }

    private MockMultipartFile getFile(String name) {
        return new MockMultipartFile(name,this.getClass().getClassLoader().getResourceAsStream("courseListParserServiceSpec/$name"))
    }
}
