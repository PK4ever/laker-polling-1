package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.domains.Role
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.RoleType
import grails.test.mixin.TestFor
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import org.springframework.http.HttpStatus
import spock.lang.Specification

@Integration
@Rollback
@TestFor(CourseService)
class CourseServiceSpec extends Specification {
    AuthToken bad_auth
    User admn, badmn, inst1, inst2, binst, a, b, c, d, bad
    Course course1, course2

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
        /* TEST AUTHTOKEN - Unassigned AuthToken */
        bad_auth = new AuthToken(accessToken: "fail", subject: "fail-subj")

        /* TEST ADMIN - Valid Email */
        admn = new User(email: "test.admin@oswego.edu")
        admn.setRole(new Role(type: RoleType.ADMIN, master: RoleType.ADMIN))
        admn.setAuthToken(new AuthToken(accessToken: "admn-1000", subject: "admn-1000-subj"))
        admn.save(flush:true, failOnError: true)

        /* TEST ADMIN - Invalid Email */
        badmn = new User(email: "bad.admin@bad.com")
        badmn.setRole(new Role(type: RoleType.ADMIN, master: RoleType.ADMIN))
        badmn.setAuthToken(new AuthToken(accessToken: "badmn-1000", subject: "badmn-1000-subj"))
        badmn.save(flush:true, failOnError: true)

        /* TEST INSTRUCTORS - Valid Emails */
        inst1 = new User(email: "test.email@oswego.edu")
        inst1.setRole(new Role(type: RoleType.INSTRUCTOR, master: RoleType.INSTRUCTOR))
        inst1.setAuthToken(new AuthToken(accessToken: "inst-1000", subject: "inst-1000-subj"))
        inst1.save(flush: true, failOnError: true)

        inst2 = new User(email: "test.email2@oswego.edu")
        inst2.setRole(new Role(type: RoleType.INSTRUCTOR, master: RoleType.INSTRUCTOR))
        inst2.setAuthToken(new AuthToken(accessToken: "inst2-1000", subject: "inst2-1000-subj"))
        inst2.save(flush: true, failOnError: true)

        /* TEST INSTRUCTOR - Invalid Email */
        binst = new User(email: "bad.inst@bad.com")
        binst.setRole(new Role(type: RoleType.INSTRUCTOR, master: RoleType.INSTRUCTOR))
        binst.setAuthToken(new AuthToken(accessToken: "binst-1000", subject: "binst-1000-subj"))
        binst.save(flush: true, failOnError: true)

        /* TEST STUDENTS - In A Course */
        a = new User(firstName: "Jason", lastName: "Parker", email: "a@oswego.edu", imageUrl: "Some image")
        a.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        a.setAuthToken(new AuthToken(subject: 'sub-a-1000', accessToken: 'aa-1000'))
        a.save(flush: true, failOnError: true)

        b = new User(firstName: "Peter", lastName: "Swanson", email: "b@oswego.edu", imageUrl: "coolest")
        b.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        b.setAuthToken(new AuthToken(subject: 'sub-b-1000', accessToken: 'bb-1000'))
        b.save(flush: true, failOnError: true)

        /* TEST STUDENTS - Not In A Course */
        c = new User(firstName: "John", lastName: "Johnson", email: "c@oswego.edu", imageUrl: "Other image")
        c.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        c.setAuthToken(new AuthToken(subject: 'sub-c-1000', accessToken: 'cc-1000'))
        c.save(flush: true, failOnError: true)

        d = new User(firstName: "Jack", lastName: "Jackson", email: "d@oswego.edu", imageUrl: "Other coolest")
        d.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        d.setAuthToken(new AuthToken(subject: 'sub-d-1000', accessToken: 'dd-1000'))
        d.save(flush: true, failOnError: true)

        /* TEST STUDENT - Invalid Email */
        bad = new User(firstName: "Bad", lastName: "Guy", email: "bad.guy@bad.com", imageUrl: "Some Other Coolest")
        bad.setRole(new Role(type: RoleType.STUDENT, master: RoleType.STUDENT))
        bad.setAuthToken(new AuthToken(subject: 'sub-bad-1000', accessToken: 'bad-1000'))
        bad.save(flush: true, failOnError: true)

        /* TEST COURSE - Contains Students a And b */
        course1 = new Course(name: "CSC 480", crn: 11111, instructor: inst1)
        course1.addToStudents(a)
        course1.addToStudents(b)
        course1.save(flush: true, failOnError: true)

        /* TEST COURSE - Contains No Students */
        course2 = new Course(name: "CSC 212", crn: 22222, instructor: inst2)
        course2.save(flush: true, failOnError: true)
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
                    count++
                    break
                case "b@oswego.edu":
                    count++
                    break
                default:
                    assert false
                    break
            }
        }
        count == 2
    }

    void "test getAllStudents(): Invalid AuthToken"() {
        when:
        prepareData()

        then:
        def users = service.getAllStudents(new AuthToken(accessToken: "fail", subject: "fail-subj"), course1.id.toString()).data
        users == null
    }

    void "test getAllStudents(): Null AuthToken"() {
        when:
        prepareData()

        then:
        def users = service.getAllStudents(null, course1.id.toString()).data
        users == null
    }

    void "test getAllStudents(): Invalid courseId"() {
        when:
        prepareData()

        then:
        def users = service.getAllStudents(inst1.getAuthToken(), "asdf").data
        users == null
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
            error = true
        }

        error
    }

    void "test deleteCourse(): All Valid EQ Classes"() {
        when:
        prepareData()

        then:
        def course = service.deleteCourse(inst1.getAuthToken(), course1.id.toString()).data
        course == course1
    }

    void "test deleteCourse(): Invalid AuthToken"() {
        when:
        prepareData()

        then:
        def course = service.deleteCourse(new AuthToken(accessToken: "fail", subject: "fail-subj"), course1.id.toString()).data
        course == null
    }

    void "test deleteCourse(): Null AuthToken"() {
        when:
        prepareData()

        then:
        def course = service.deleteCourse(null, course1.id.toString()).data
        course == null
    }

    void "test deleteCourse(): Invalid courseId"() {
        when:
        prepareData()

        then:
        def course = service.deleteCourse(inst1.getAuthToken(), "asdf").data
        course == null
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
            error = true
        }

        error
    }

    void "test postStudentsToCourse(): All Valid EQ Classes"() {
        when:
        prepareData()

        then:
        def students = [c.getEmail(), d.getEmail()]
        println students
        def returned_students = service.postStudentsToCourse(inst1.getAuthToken(), course1.getId().toString(), students).data

        println returned_students

//        returned_students.get(0).getEmail() == "c@oswego.edu"
//        returned_students.get(1).getEmail() == "d@oswego.edu"
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

    void "test deleteStudentCourse(): Invalid list of user ids"() {
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

    void "test deleteStudentCourse(): Null list of user ids"() {
        when:
        prepareData()

        then:
        service.deleteStudentCourse(inst1.getAuthToken(), course1.getId(), null)
        service.getAllStudents(inst1.getAuthToken(), course1.getId().toString()).data.size() == 2
    }

    void "test deleteStudentCourse(): Empty list of user ids"() {
        when:
        prepareData()

        then:
        service.deleteStudentCourse(inst1.getAuthToken(), course1.getId(), [])
        service.getAllStudents(inst1.getAuthToken(), course1.getId().toString()).data.size() == 2
    }

    void "test instructorCreateCourse(): All Valid EQ Classes"() {
        when:
        prepareData()

        then:
        def new_course = service.instructorCreateCourse(inst1.getAuthToken(), "123456", "test_course").data
        new_course.getCrn() == "123456"
        new_course.getInstructor() == inst1
        new_course.getName() == "test_course"
        new_course.getStudents() == null
        new_course.getQuestions() == null
    }

    void "test instructorCreateCourse(): Invalid AuthToken"() {
        when:
        prepareData()

        then:
        def error = false
        try {
            service.instructorCreateCourse(new AuthToken(accessToken: "fail", subject: "fail-subj"), "123456", "test_course")
        }
        catch(NullPointerException npe) {
            error = true
        }
        error
    }

    void "test instructorCreateCourse(): Null AuthToken"() {
        when:
        prepareData()

        then:
        def error = false
        try {
            service.instructorCreateCourse(null, "123456", "test_course")
        }
        catch(NullPointerException npe) {
            error = true
        }
        error
    }

    void "test instructorCreateCourse(): Invalid crn"() {
        when:
        prepareData()

        then:
        def new_course = service.instructorCreateCourse(inst1.getAuthToken(), "asdf", "test_course").data
        new_course.crn == "asdf"
    }

    void "test instructorCreateCourse(): Empty crn"() {
        when:
        prepareData()

        then:
        def new_course = service.instructorCreateCourse(inst1.getAuthToken(), "", "test_course").data
        new_course.crn == ""
    }

    void "test instructorCreateCourse(): Empty course name"() {
        when:
        prepareData()

        then:
        def new_course = service.instructorCreateCourse(inst1.getAuthToken(), "123456", "").data
        new_course.name == ""
    }

    void "test adminCreateCourse(): All Valid EQ Classes"() {
        when:
        prepareData()

        then:
        def new_course = service.adminCreateCourse(admn.getAuthToken(), "123456", "test_course", inst1.getId().toString()).data
        new_course.getCrn() == "123456"
        new_course.getInstructor() == inst1
        new_course.getName() == "test_course"
        new_course.getStudents() == null
        new_course.getQuestions() == null
    }

    void "test adminCreateCourse(): Invalid AuthToken"() {
        when:
        prepareData()

        then:
        def new_course = service.adminCreateCourse(new AuthToken(accessToken: "fail", subject: "fail-subj"), "123456", "test_course", inst1.getId().toString()).data
        new_course == null
    }

    void "test adminCreateCourse(): Null AuthToken"() {
        when:
        prepareData()

        then:
        def new_course = service.adminCreateCourse(null, "123456", "test_course", inst1.getId().toString()).data
        new_course == null
    }

    void "test adminCreateCourse(): Invalid crn"() {
        when:
        prepareData()

        then:
        def new_course = service.adminCreateCourse(admn.getAuthToken(), "asdf", "test_course", inst1.getId().toString()).data
        new_course.crn == "asdf"
    }

    void "test adminCreateCourse(): Empty crn"() {
        when:
        prepareData()

        then:
        def new_course = service.adminCreateCourse(admn.getAuthToken(), "", "test_course", inst1.getId().toString()).data
        new_course.crn == ""
    }

    void "test adminCreateCourse(): Invalid instructor id"() {
        when:
        prepareData()

        then:
        def new_course = service.adminCreateCourse(admn.getAuthToken(), "asdf", "test_course", "123141389273").data
        new_course == null
    }

    void "test adminCreateCourse(): Empty instructor id"() {
        when:
        prepareData()

        then:
        def error = false
        try {
            service.adminCreateCourse(admn.getAuthToken(), "asdf", "test_course", "")
        }
        catch(NumberFormatException nfe) {
            error = true
        }
        error
    }

    void "test adminCreateCourse(): Empty course name"() {
        when:
        prepareData()

        then:
        def new_course = service.adminCreateCourse(admn.getAuthToken(), "asdf", "", inst1.getId().toString()).data
        new_course.name == ""
    }

    void "test delete(): All Valid EQ Classes"() {
        when:
        prepareData()

        then:
        def old_courses = service.getAllCourses(inst1.getAuthToken()).data
        old_courses == [course1]
        service.delete(course1.getId().toInteger().intValue())
        def new_courses = service.getAllCourses(inst1.getAuthToken()).data
        new_courses == []
    }

    void "test delete(): Invalid course id"() {
        when:
        prepareData()

        then:
        def old_courses = service.getAllCourses(inst1.getAuthToken()).data
        old_courses == [course1]
        service.delete(1231521313)
        def new_courses = service.getAllCourses(inst1.getAuthToken()).data
        new_courses == [course1]
    }

    void "test isInstructorOf(): All Valid EQ Classes"() {
        when:
        prepareData()

        then:
        service.isInstructorOf(inst1, course1)
    }

    void "test isInstructorOf(): Invalid Instructor"() {
        when:
        prepareData()

        then:
        !service.isInstructorOf(inst2, course1)
    }

    void "test isInstructorOf(): Null Instructor"() {
        when:
        prepareData()

        then:
        !service.isInstructorOf(null, course1)
    }

    void "test isInstructorOf(): Invalid Course"() {
        when:
        prepareData()

        then:
        !service.isInstructorOf(inst1, course2)
    }

    void "test isInstructorOf(): Null Course"() {
        when:
        prepareData()

        then:
        !service.isInstructorOf(inst1, null)
    }

    void "test getAllCourses(): All Valid EQ Classes"() {
        when:
        prepareData()

        then:
        def courses = service.getAllCourses(inst1.getAuthToken()).data
        courses == [course1]
    }

    void "test getAllCourses(): Invalid AuthToken"() {
        when:
        prepareData()

        then:
        def courses = service.getAllCourses(new AuthToken(accessToken: "fail", subject: "fail-subj")).data
        courses == null
    }

    void "test getAllCourses(): Null AuthToken"() {
        when:
        prepareData()

        then:
        def courses = service.getAllCourses(null).data
        courses == null
    }

    void "test getAllCourses(2): All Valid EQ Classes"() {
        when:
        prepareData()

        then:
        def courses = service.getAllCourses(inst1.getAuthToken(), course1.getId().toString()).data
        courses == [course1]
    }

    void "test getAllCourses(2): Invalid AuthToken"() {
        when:
        prepareData()

        then:
        def courses = service.getAllCourses(new AuthToken(accessToken: "fail", subject: "fail-subj"), course1.getId().toString()).data
        courses == null
    }

    void "test getAllCourses(2): Null AuthToken"() {
        when:
        prepareData()

        then:
        def courses = service.getAllCourses(null, course1.getId().toString()).data
        courses == null
    }

    void "test getAllCourses(2): Invalid course id"() {
        when:
        prepareData()

        then:
        def courses = service.getAllCourses(inst1.getAuthToken(), "12340189273").data
        courses == [null]
    }

    void "test findCourse(): All Valid EQ Classes"() {
        when:
        prepareData()

        then:
        def course = service.findCourse(course1.getId().toString()).data
        course.id == course1.id
    }

    void "test findCourse(): Invalid course id"() {
        when:
        prepareData()

        then:
        def course = service.findCourse("123124129").data
        course == null
    }

    void "test findCourse(): Empty course id"() {
        when:
        prepareData()

        then:
        def course = service.findCourse("").data
        course == null
    }

    void "test verifyStudentAccess(): All Valid EQ Classes"() {
        when:
        prepareData()

        then:
        def qd = service.verifyStudentAccess(a.getAuthToken(), course1)
        qd.success
    }

    void "test verifyStudentAccess(): No-Auth Student"() {
        when:
        prepareData()

        then:
        def qd = service.verifyStudentAccess(c.getAuthToken(), course1)
        !qd.success
        qd.errorCode == HttpStatus.UNAUTHORIZED.value()
    }

    void "test verifyStudentAccess(): Invalid AuthToken"() {
        when:
        prepareData()

        then:
        def qd = service.verifyStudentAccess(bad_auth, course1)
        !qd.success
        qd.errorCode == HttpStatus.BAD_REQUEST.value()
    }
}
