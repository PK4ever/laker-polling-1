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

    User inst1, a, b, c, d
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

        /* TEST STUDENTS - IN A COURSE */
        a = new User(firstName: "Jason", lastName: "Parker", email: "a@oswego.edu", imageUrl: "Some image")
        a.setRole(new Role(type: RoleType.STUDENT))
        a.setAuthToken(new AuthToken(subject: 'sub-a-1000', accessToken: 'aa-1000'))
        a.save(flush: true, failOnError: true)

        b = new User(firstName: "Peter", lastName: "Swanson", email: "b@oswego.edu", imageUrl: "coolest")
        b.setRole(new Role(type: RoleType.STUDENT))
        b.setAuthToken(new AuthToken(subject: 'sub-b-1000', accessToken: 'bb-1000'))
        b.save(flush: true, failOnError: true)

        /* TEST STUDENTS - NOT IN A COURSE */
        c = new User(firstName: "John", lastName: "Johnson", email: "c@oswego.edu", imageUrl: "Other image")
        c.setRole(new Role(type: RoleType.STUDENT))
        c.setAuthToken(new AuthToken(subject: 'sub-c-1000', accessToken: 'cc-1000'))
        c.save(flush: true, failOnError: true)

        d = new User(firstName: "Jack", lastName: "Jackson", email: "d@oswego.edu", imageUrl: "Other coolest")
        d.setRole(new Role(type: RoleType.STUDENT))
        d.setAuthToken(new AuthToken(subject: 'sub-d-1000', accessToken: 'dd-1000'))
        d.save(flush: true, failOnError: true)

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
                    println "Found email that was not supposed to exist"
                    assert false
                    break
            }
        }
        if(count != 2) {
            println "Did not find all expected emails"
            assert false
        }
    }

    void "test getAllStudents(): Invalid AuthToken"() {
        when:
        prepareData()

        then:
        def users = service.getAllStudents(new AuthToken(accessToken: "fail", subject: "fail-subj"), course1.id.toString()).data
        if(users != null) {
            println "Should not find any students without proper AuthToken"
            assert false
        }
        else println "Null list, pass"
    }

    void "test getAllStudents(): Null AuthToken"() {
        when:
        prepareData()

        then:
        def users = service.getAllStudents(null, course1.id.toString()).data
        if(users != null) {
            println "Should not find any students without an AuthToken"
            assert false
        }
        else println "Null list, pass"
    }

    void "test getAllStudents(): Invalid courseId"() {
        when:
        prepareData()

        then:
        def users = service.getAllStudents(inst1.getAuthToken(), "asdf").data
        if(users != null) {
            println "Course shouldn't be findable"
            assert false
        }
        else println "Null list, pass"
    }

    void "test getAllStudents(): Null courseId"() {
        when:
        prepareData()

        then:
        def error = false
        try {
            service.getAllStudents(inst1.getAuthToken(), null).data
        }
        catch(NullPointerException npe) {
            println "Error caught, pass"
            error = true
        }

        if(!error) {
            println "Null courseId shouldn't be able to have methods invoked on it"
            assert false
        }
    }

    void "test deleteCourse(): All Valid EQ Classes"() {
        when:
        prepareData()

        then:
        def course = service.deleteCourse(inst1.getAuthToken(), course1.id.toString()).data
        if(course != course1) {
            println "Deleted course didn't match course that was created"
            assert false
        }
        else {
            println "Deleted course matched, pass"
        }
    }

    void "test deleteCourse(): Invalid AuthToken"() {
        when:
        prepareData()

        then:
        def course = service.deleteCourse(new AuthToken(accessToken: "fail", subject: "fail-subj"), course1.id.toString()).data
        if(course != null) {
            println "Should not find a course without proper AuthToken"
            assert false
        }
        else println "Null course, pass"
    }

    void "test deleteCourse(): Null AuthToken"() {
        when:
        prepareData()

        then:
        def course = service.deleteCourse(null, course1.id.toString()).data
        if(course != null) {
            println "Should not find a course with a null AuthToken"
            assert false
        }
        else println "Null course, pass"
    }

    void "test deleteCourse(): Invalid courseId"() {
        when:
        prepareData()

        then:
        def users = service.deleteCourse(inst1.getAuthToken(), "asdf").data
        if(users != null) {
            println "Course shouldn't be findable"
            assert false
        }
        else println "Null list, pass"
    }

    void "test deleteCourse(): Null courseId"() {
        when:
        prepareData()

        then:
        def error = false
        try {
            service.deleteCourse(inst1.getAuthToken(), null).data
        }
        catch(NullPointerException npe) {
            println "Error caught, pass"
            error = true
        }

        if(!error) {
            println "Null courseId shouldn't be able to have methods invoked on it"
            assert false
        }
    }

    void "test postStudentsToCourse(): All Valid EQ Classes"() {
        when:
        prepareData()

        then:
        def students = [c.getEmail(), d.getEmail()]
        def returned_students = service.postStudentsToCourse(inst1.getAuthToken(), course1.getId().toString(), students).data

        returned_students.get(0).getEmail() == "c@oswego.edu"
        returned_students.get(1).getEmail() == "d@oswego.edu"
    }

    void "test postStudentsToCourse(): Invalid AuthToken"() {
        when:
        prepareData()

        then:
        def students = [c.getEmail(), d.getEmail()]
        def returned_students = service.postStudentsToCourse(new AuthToken(accessToken: "fail", subject: "fail-subj"), course1.getId().toString(), students).data
        returned_students == null
    }

    void "test postStudentsToCourse(): Null AuthToken"() {
        when:
        prepareData()

        then:
        def students = [c.getEmail(), d.getEmail()]
        def returned_students = service.postStudentsToCourse(null, course1.getId().toString(), students).data
        returned_students == null
    }

    void "test postStudentsToCourse(): Invalid courseId"() {
        when:
        prepareData()

        then:
        def students = [c.getEmail(), d.getEmail()]
        def returned_students = service.postStudentsToCourse(inst1.getAuthToken(), "asdf", students).data
        returned_students == null
    }

    void "test postStudentsToCourse(): Null courseId"() {
        when:
        prepareData()

        then:
        def students = [c.getEmail(), d.getEmail()]
        def returned_students = service.postStudentsToCourse(inst1.getAuthToken(), "asdf", students).data
        returned_students == null
    }

    void "test postStudentsToCourse(): Invalid list of emails"() {
        when:
        prepareData()

        then:
        def students = ["", "test@gmail.com", "asdf"]
        def error = false
        try {
            service.postStudentsToCourse(inst1.getAuthToken(), course1.getId().toString(), students).data
        }
        catch(NullPointerException npe) {
            error = true
        }

        error
    }

    void "test postStudentsToCourse(): Null list of emails"() {
        when:
        prepareData()

        then:
        def returned_students = service.postStudentsToCourse(inst1.getAuthToken(), course1.getId().toString(), null).data
        returned_students == []
    }

    void "test postStudentsToCourse(): Empty list of emails"() {
        when:
        prepareData()

        then:
        def returned_students = service.postStudentsToCourse(inst1.getAuthToken(), course1.getId().toString(), []).data
        returned_students == []
    }

    void "test deleteStudentCourse(): All Valid EQ Classes"() {
        when:
        prepareData()

        then:
        def students = [a.getId().toString(), b.getId().toString()]
        service.deleteStudentCourse(inst1.getAuthToken(), course1.getId(), students)
        service.getAllStudents(inst1.getAuthToken(), course1.getId().toString()).data.size() == 0
    }

    void "test deleteStudentCourse(): Invalid AuthToken"() {
        when:
        prepareData()

        then:
        def students = [a.getId().toString(), b.getId().toString()]
        service.deleteStudentCourse(new AuthToken(accessToken: "fail", subject: "fail-subj"), course1.getId(), students)
        service.getAllStudents(inst1.getAuthToken(), course1.getId().toString()).data.size() == 2
    }

    void "test deleteStudentCourse(): Null AuthToken"() {
        when:
        prepareData()

        then:
        def students = [a.getId().toString(), b.getId().toString()]
        service.deleteStudentCourse(null, course1.getId(), students)
        service.getAllStudents(inst1.getAuthToken(), course1.getId().toString()).data.size() == 2
    }

    void "test deleteStudentCourse(): Invalid courseId"() {
        when:
        prepareData()

        then:
        def students = [a.getId().toString(), b.getId().toString()]
        service.deleteStudentCourse(inst1.getAuthToken(), 132123123, students)
        service.getAllStudents(inst1.getAuthToken(), course1.getId().toString()).data.size() == 2
    }

    void "test deleteStudentCourse(): Invalid list of ids"() {
        when:
        prepareData()

        then:
        def students = ["12931093", "12309750172"]
        def error = false
        try {
            service.deleteStudentCourse(inst1.getAuthToken(), course1.getId(), students)
        }
        catch(IllegalArgumentException iae) {
            error = true
        }

        error
    }

    void "test deleteStudentCourse(): Null list of emails"() {
        when:
        prepareData()

        then:
        service.deleteStudentCourse(inst1.getAuthToken(), course1.getId(), null)
        service.getAllStudents(inst1.getAuthToken(), course1.getId().toString()).data.size() == 2
    }

    void "test deleteStudentCourse(): Empty list of emails"() {
        when:
        prepareData()

        then:
        service.deleteStudentCourse(inst1.getAuthToken(), course1.getId(), [])
        service.getAllStudents(inst1.getAuthToken(), course1.getId().toString()).data.size() == 2
    }
}
