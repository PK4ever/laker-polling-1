package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.domains.Role
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.RoleType
import grails.test.mixin.TestFor
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@Integration
@Rollback
@TestFor(CourseService)
class CourseServiceSpec extends Specification {

    User inst1
    Course course1

    def setup() {
        println "Starting CourseService Tests"
    }

    def cleanup() {
        println "Stopping CourseService Tests"
    }

    /* Necessary for populating the in-memory database.  Mocks do not work, since they will null all values, and
    self-destruct itself upon trying to set values (no clue why).  This means that we are going to have to use the
    Integration test annotation with the rollback to ensure that it reverts changes to the db (seemed to bug out without
    this annotation).  This method allows you to just call it at the start of every test under the when: tag, and
    populate the db on each trial. */
    def prepareData() {
        /* TEST INSTRUCTOR */
        inst1 = new User(email: "test.email@oswego.edu")
        inst1.setRole(new Role(type: RoleType.INSTRUCTOR))
        inst1.setAuthToken(new AuthToken(accessToken: "inst-1000", subject: "inst-1000-subj"))
        inst1.save(flush: true, failOnError: true)

        /* TEST STUDENTS */
        User a = new User(firstName: "Jason", lastName: "Parker", email: "a@oswego.edu", imageUrl: "Some image")
        a.setRole(new Role(type: RoleType.STUDENT))
        a.setAuthToken(new AuthToken(subject: 'sub-a-1000', accessToken: 'aa-1000'))
        a.save(flush: true, failOnError: true)

        User b = new User(firstName: "Peter", lastName: "Swanson", email: "b@oswego.edu", imageUrl: "coolest")
        b.setRole(new Role(type: RoleType.STUDENT))
        b.setAuthToken(new AuthToken(subject: 'sub-b-1000', accessToken: 'bb-1000'))
        b.save(flush: true, failOnError: true)

        /* TEST COURSE */
        course1 = new Course(name: "CSC 480", crn: 11111, instructor: inst1)
        course1.addToStudents(a)
        course1.addToStudents(b)
        course1.save(flush: true, failOnError: true)
    }

    void "test getAllStudents(): All Valid EQ Classes"() {
        when:
        prepareData()

        then:
        def users = service.getAllStudents(inst1.getAuthToken(), course1.id.toString()).data

        /* Unfortunately, unless explicitly defined in the domain itself, getting a list of the results returns an
        unordered list. This means that querying for the value at a certain location is impossible. Workaround for
        now could be an iteration loop that has switch statements for the expected values, and a count checker to
        crosscheck that both expected values were hit.  Upside, it allows you to detect if extraneous values were
        passed in, so you could fail it on those occasions.  Example of this is used below. */
        def count = 0
        for(user in users) {
            switch(user.getEmail()) {
                case "a@oswego.edu":
                    println "Found a@oswego.edu"
                    count++
                    break
                case "b@oswego.edu":
                    println "Found b@oswego.edu"
                    count++
                    break
                default:
                    fail("Found email that was not supposed to exist")
            }
        }
        if(count != 2) {
            fail("Did not find all expected emails")
        }
    }
}
