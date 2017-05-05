package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.BootStrapSpec
import edu.oswego.cs.lakerpolling.domains.Quiz
import edu.oswego.cs.lakerpolling.domains.User

import java.text.SimpleDateFormat

class CourseControllerSpec extends BootStrapSpec {

    /**
     * Tests for courseGet(String access_token, String course_id)
     * Endpoint to GET a course or list of courses
     * @param access_token - the access token of the requesting user
     * @param course_id - only needed when searching for a specific course. otherwise input as null
     * */

    void "test courseGet(): Valid parameters" () {

    	given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", VALID_COURSE.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "Course get is Queried"
        def response = get("/api/course", params)
        def course = response.json.data.courses[0]

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
    }



//     /** XXX **/

    void "test courseGet: Invalid eq classes " () {

    	given: "The Following Parameters"
        testWith(INVALID_INSTRUCTOR, INVALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", INVALID_COURSE.id)
        params.put("access_token", INVALID_INSTRUCTOR.authToken.accessToken)

        when: "Course get is Queried"
        def response = get("/api/course", params)

        then: "The Output Should Be The Following"
        response.status == 400
        

    }



    /**
     * Tests for: postCourse(String access_token, String crn, String name, String user_id) 
     * Endpoint to POST a new course to the server
     * @param access_token - The access token of the requesting user
     * @param crn - the id of the course being added
     * @param name - the name of the course being added
     * @param user_id - the user id of the instructor the course will be added to
     * ["access_token", "crn", "name"]
     */

     // As instructor
    void "test postCourse(): Valid parameters instructor" () {

    	given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", VALID_COURSE.id)
        params.put("crn", VALID_COURSE.crn)
        params.put("name", VALID_COURSE.name)
        params.put("user_id", VALID_INSTRUCTOR.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "Course get is posted"
        def response = post("/api/course", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"

    }

    void "test postCourse(): Valid parameters ADMIN" () {

        given: "The Following Parameters"
        testWith(VALID_ADMIN, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", VALID_COURSE.id)
        params.put("crn", VALID_COURSE.crn)
        params.put("name", VALID_COURSE.name)
        params.put("user_id", VALID_ADMIN.id)
        params.put("access_token", VALID_ADMIN.authToken.accessToken)

        when: "Course get is posted"
        def response = post("/api/course", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"

    }


// //     /** XXX **/

    void "test postCourse(): invalid eq classes " () {

        given: "The Following Parameters"
        testWith(INVALID_ADMIN, INVALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", INVALID_COURSE.id)
        params.put("crn", INVALID_COURSE.crn)
        params.put("name", INVALID_COURSE.name)
        params.put("user_id", INVALID_ADMIN.id)
        params.put("access_token", INVALID_ADMIN.authToken.accessToken)

        when: "Course get is posted"
        def response = post("/api/course", params)

        then: "The Output Should Be The Following"
        response.status == 401

    }


//     /**
//      * Test for: deleteCourse(String access_token, String course_id)
//      * Endpoint to perform delete operation active courses.
//      * @param access_token - The access token of the requesting user.
//      * @param course_id - The id of the course.
// 		* ["access_token", "course_id"]
//      */

    void "test deleteCourse(): Valid parameters ADMIN" () {

    	given: "The Following Parameters"
        testWith(VALID_ADMIN, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", VALID_COURSE.id)
        params.put("access_token", VALID_ADMIN.authToken.accessToken)

        when: "Course get is deleted"
        def response = delete("/api/course", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
    }

    void "test deleteCourse(): Valid parameters INSTRUCTOR" () {

        given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", VALID_COURSE.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "Course get is deleted"
        def response = delete("/api/course", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
    }

// //     /** XXX **/

    void "test deleteCourse(): Invalid eq classes " () {

    	given: "The Following Parameters"
        testWith(INVALID_INSTRUCTOR, INVALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", INVALID_COURSE.id)
        params.put("access_token", INVALID_INSTRUCTOR.authToken.accessToken)

        when: "Course get is deleted"
        def response = delete("/api/course", params)

        then: "The Output Should Be The Following"
        response.status == 401
    }



     /**
     * Test for: getCourseStudent(String access_token, String course_id) 
     * Endpoint to get a list of students in a specified course.
     * @param access_token - The access token of the requesting user.
     * @param course_id - The id of the course
     */

     void "test getCourseStudent(): Valid parameters " () {

    	given: "The Following Parameters"
        testWith(VALID_ADMIN, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", VALID_COURSE.id)
        params.put("access_token", VALID_ADMIN.authToken.accessToken)

        when: "Course list get is retrieved"
        def response = get("/api/course/student", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
    }

//     // /** XXX **/

    void "test getCourseStudent(): Invalid eq classes " () {

    	given: "The Following Parameters"
        testWith(INVALID_ADMIN, INVALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", INVALID_COURSE.id)
        params.put("access_token", INVALID_ADMIN.authToken.accessToken)

        when: "Course list get is retrieved"
        def response = get("/api/course/student", params)

        then: "The Output Should Be The Following"
        response.status == 401
    }


    /**
     * Test for: postCourseStudent(String access_token, String course_id, String email) 
     * Endpoint to add students to an existing course by their email address. The POST request can also take a CSV file
     * containing student emails. If this CSV file is included in the request then it will be parsed and the students
     * associated with each of the emails in the file will be added to the course.
     * @param access_token - The access token of the requesting user
     * @param course_id - the id of the course being added
     * @param email - the name of an email address by which to add a student
     * ["access_token", "course_id"]
    */
     
	void "test postCourseStudent(): Valid parameters " () {

    	given: "The Following Parameters"
        testWith(VALID_ADMIN, VALID_COURSE, VALID_STUDENT)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", VALID_COURSE.id)
        params.put("email", VALID_STUDENT.email)
        params.put("access_token", VALID_ADMIN.authToken.accessToken)

        when: "Course get is posted"
        def response = post("/api/course/student", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"

    }

//     /** XXX **/

    void "test postCourseStudent(): Invalid eq classes " () {

    	given: "The Following Parameters"
        testWith(INVALID_ADMIN, INVALID_COURSE, INVALID_STUDENT)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", INVALID_COURSE.id)
        params.put("email", INVALID_STUDENT.email)
        params.put("access_token", INVALID_ADMIN.authToken.accessToken)

        when: "Course get is posted"
        def response = post("/api/course/student", params)

        then: "The Output Should Be The Following"
        response.status == 401
    }


    /**
     * Test for: deleteCourseStudent(String access_token, String course_id, String user_id)
     * @param access_token - The access token of the requesting user
     * @param course_id - the id of the course being added
     * @param user_id - the user id of the instructor the course will be added to
     * ["access_token", "course_id", "user_id"]
     */

     void "test deleteCourseStudent(): Valid parameters " () {

    	given: "The Following Parameters"
        testWith(INVALID_ADMIN, INVALID_COURSE, INVALID_STUDENT)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", INVALID_COURSE.id)
        params.put("email", INVALID_STUDENT.id)
        params.put("access_token", INVALID_ADMIN.authToken.accessToken)

        when: "Course get is posted"
        def response = post("/api/course/student", params)

        then: "The Output Should Be The Following"
        response.status == 200

    }

//     /** XXX **/

    void "test deleteCourseStudent(): Invalid eq classes " () {

    	given: "The Following Parameters"
        testWith(INVALID_ADMIN, INVALID_COURSE, INVALID_STUDENT)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", INVALID_COURSE.id)
        params.put("email", INVALID_STUDENT.id)
        params.put("access_token", INVALID_ADMIN.authToken.accessToken)

        when: "Course get is posted"
        def response = post("/api/course/student", params)

        then: "The Output Should Be The Following"
        response.status == 401
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
        given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", VALID_COURSE.id)
        params.put("date", VALID_COURSE.crn)
        params.put("start_date", VALID_COURSE.name)
        params.put("end_date", VALID_COURSE.name)
        params.put("student_id", VALID_INSTRUCTOR.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "Course get is posted"
        def response = post("/api/course/attendance", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
    	
    }

    void "test getAttendance(): invalid eq classes " () {
        given: "The Following Parameters"
        testWith(INVALID_INSTRUCTOR, INVALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", INVALID_COURSE.id)
        params.put("date", INVALID_COURSE.crn)
        params.put("start_date", INVALID_COURSE.name)
        params.put("end_date", INVALID_COURSE.name)
        params.put("student_id", INVALID_INSTRUCTOR.id)
        params.put("access_token", INVALID_INSTRUCTOR.authToken.accessToken)

        when: "Course get is posted"
        def response = post("/api/course/attendance", params)

        then: "The Output Should Be The Following"
        response.status == 404
        
    }



}

