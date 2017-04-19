package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.domains.Role
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.RoleType

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(CourseController)
class CourseControllerSpec extends Specification {

	User inst1
    Course course1 

    def setup() {
    	println "Starting CourseController Tests"
    }

    def cleanup() {
    	println "Ending CourseController Tests"
    }

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

        course2 = new Course(name: "CSC 485", crn: 11111, instructor: inst1)
        course2.addToStudents(a)
        course2.addToStudents(b)
        course2.save(flush: true, failOnError: true)
    }

    /**
     * Tests for courseGet(String access_token, String course_id)
     * Endpoint to GET a course or list of courses
     * @param access_token - the access token of the requesting user
     * @param course_id - only needed when searching for a specific course. otherwise input as null
     * */

    void "test courseGet(): Valid parameters (Single course)" () {

    	when:
    	prepareData()
    	controller.courseGet("inst-1000" , course1.id.toString())

    	then:

    	def courses = model.courses

    	def count = 0
        for(course in courses) {
            switch(course.id.toString()) {
                case course1.id.toString():
                    println "Found correct single course"
                    count++
                    break
                default:
                    println("Found course that was not supposed to exist")
                    assert false 
                    break
            }
        }

        if(count != 1) {
            println("Did not find all expected emails")
            assert false
        }
    }

    void "test courseGet: Valid parameters (Multiple courses)" () {

    	when:
    	prepareData()
    	controller.courseGet("inst-1000" , null)

    	then:
    	def courses = model.courses

    	for(course in courses) {
            switch(course.id.toString()) {
                case course1.id.toString():
                    println "Found correct course"
                    count++
                    break
                case course2.id.toString():
                    println "Found correct course"
                    count++
                    break
                default:
                    println("Found course that was not supposed to exist")
                    assert false 
                    break
            }
        }

        if(count != 2) {
            println("Did not find all expected emails")
            assert false
        }



    }

    void "test courseGet: Invalid access_token " () {

    	when:
    	prepareData()
    	controller.courseGet("ajdsfbks" , course1.id.toString())

    	then:
		1==1
    	
    	
    }

    void "test courseGet: Invalid course_id " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.courseGet("inst-1000" , "ajdsfbks")
    	
    }

    void "test courseGet: Null course_id " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.courseGet("inst-1000" , null)
    }

    /**
     * Tests for: postCourse(String access_token, String crn, String name, String user_id) 
     * Endpoint to POST a new course to the server
     * @param access_token - The access token of the requesting user
     * @param crn - the id of the course being added
     * @param name - the name of the course being added
     * @param user_id - the user id of the instructor the course will be added to
     */

    void "test postCourse(): Valid parameters " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourse("inst-1000" , "11111", "CSC 480", inst1.id.toString)
    }

    /** XXX **/

    void "test postCourse(): Invalid access_token " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourse("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test postCourse(): null access_token " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourse("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test postCourse(): Invalid crn " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourse("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test postCourse(): null crn " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourse("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test postCourse(): Invalid name " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourse("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test postCourse(): null name " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourse("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test postCourse(): Invalid user_id " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourse("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test postCourse(): null user_id " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourse("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    /**
     * Test for: deleteCourse(String access_token, String course_id)
     * Endpoint to perform delete operation active courses.
     * @param access_token - The access token of the requesting user.
     * @param course_id - The id of the course.
     */

     void "test deleteCourse(): Valid parameters " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.deleteCourse("inst-1000" , course1.id.toString())
    }

    /** XXX **/

    void "test deleteCourse(): Invalid access_token " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.deleteCourse("inst-1000" , course1.id.toString())
    }

    void "test deleteCourse(): null access_token " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.deleteCourse("inst-1000" , course1.id.toString())
    }

    void "test deleteCourse(): Invalid crn " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.deleteCourse("inst-1000" , course1.id.toString())
    }

    void "test deleteCourse(): null crn " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.deleteCourse("inst-1000" , course1.id.toString())
    }

     /**
     * Test for: getCourseStudent(String access_token, String course_id) 
     * Endpoint to get a list of students in a specified course.
     * @param access_token - The access token of the requesting user.
     * @param course_id - The id of the course
     */

     void "test getCourseStudent(): Valid parameters " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getCourseStudent("inst-1000" , course1.id.toString())
    }

    /** XXX **/

    void "test getCourseStudent(): Invalid access_token " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getCourseStudent("inst-1000" , course1.id.toString())
    }

    void "test getCourseStudent(): null access_token " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getCourseStudent("inst-1000" , course1.id.toString())
    }

    void "test getCourseStudent(): Invalid crn " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getCourseStudent("inst-1000" , course1.id.toString())
    }

    void "test getCourseStudent(): null crn " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getCourseStudent("inst-1000" , course1.id.toString())
    }

    /**
     * Test for: postCourseStudent(String access_token, String course_id, String email) 
     * Endpoint to add students to an existing course by their email address. The POST request can also take a CSV file
     * containing student emails. If this CSV file is included in the request then it will be parsed and the students
     * associated with each of the emails in the file will be added to the course.
     * @param access_token - The access token of the requesting user
     * @param course_id - the id of the course being added
     * @param email - the name of an email address by which to add a student
     */
     
	void "test postCourseStudent(): Valid parameters " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourseStudent("inst-1000" , "11111", "CSC 480", inst1.id.toString)
    }

    /** XXX **/

    void "test postCourseStudent(): Invalid access_token " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourseStudent("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test postCourseStudent(): null access_token " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourseStudent("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test postCourseStudent(): Invalid course_id " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourseStudent("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test postCourseStudent(): null course_id " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourseStudent("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test postCourseStudent(): Invalid email " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourseStudent("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test postCourseStudent(): null email " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.postCourseStudent("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    /**
     * Test for: deleteCourseStudent(String access_token, String course_id, String user_id)
     * @param access_token - The access token of the requesting user
     * @param course_id - the id of the course being added
     * @param user_id - the user id of the instructor the course will be added to
     */

     void "test deleteCourseStudent(): Valid parameters " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.deleteCourseStudent("inst-1000" , "11111", "CSC 480", inst1.id.toString)
    }

    /** XXX **/

    void "test deleteCourseStudent(): Invalid access_token " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.deleteCourseStudent("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test deleteCourseStudent(): null access_token " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.deleteCourseStudent("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test deleteCourseStudent(): Invalid course_id " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.deleteCourseStudent("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test deleteCourseStudent(): null course_id " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.deleteCourseStudent("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test deleteCourseStudent(): Invalid user_id " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.deleteCourseStudent("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test deleteCourseStudent(): null user_id " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.deleteCourseStudent("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    /**
     * getAttendance(String access_token, String course_id, String student_id, String date, String start_date, String end_date)
     * Gets the attendance for a selected course and date or for a selected course, student, and range of dates
     * @param access_token - the access_token of the user
     * @param course_id - the course id of the course being selected
     * @param student_id - the student's id of the student being selected
     * @param date - the single date selected for a course
     * @param start_date - the start date of the range of dates selected
     * @param end_date - the end date of the range of dates selected
     * @return - returns a json view
     */

     void "test getAttendance(): Valid parameters " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getAttendance("inst-1000" , "11111", "CSC 480", inst1.id.toString)
    }

    /** XXX **/

    void "test getAttendance(): Invalid access_token " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getAttendance("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test getAttendance(): null access_token " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getAttendance("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test getAttendance(): Invalid course_id " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getAttendance("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test getAttendance(): null course_id " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getAttendance("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test getAttendance(): Invalid student_id " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getAttendance("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test getAttendance(): null student_id " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getAttendance("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test getAttendance(): Invalid date " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getAttendance("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test getAttendance(): null date " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getAttendance("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }


    void "test getAttendance(): Invalid start_date " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getAttendance("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test getAttendance(): null start_date " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getAttendance("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test getAttendance(): Invalid end_date " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getAttendance("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }

    void "test getAttendance(): null end_date " () {

    	when:
    	prepareData()

    	then:
    	def courses = controller.getAttendance("insdst-10sd00" , "11111", "CSC 480", inst1.id.toString)
    }







}
