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

    void "test courseGet(): Valid parameters (Single course)" () {

    	given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", VALID_COURSE.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "Course get is Queried"
        def response = post("/api/course", params)
        def course = response.json.data.courses[0]

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
        2.times {
            def validCourse
            if(course.name == VALID_COURSE.name) {
                validCourse = VALID_COURSE
            } else {
                validCourse = VALID_COURSE2
            }

            assert course.name == validCourse.name
            assert course.id == validCourse.id

            course = response.json.data.courses[1]
        }
    }

//     void "test courseGet: Valid parameters (Multiple courses)" () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	controller.courseGet("inst-1000" , null)

//     	then:
//     	def courses = model.courses
//     	def count = 0

//     	for(course in courses) {
//             switch(course.id.toString()) {
//                 case course1.id.toString():
//                     count++
//                     break
//                 case course2.id.toString():
//                     count++
//                     break
//                 default:
//                     println("Found course that was not supposed to exist")
//                     assert false 
//                     break
//             }
//         }

//         if(count != 2) {
//             println("Did not find all expected emails")
//             assert false
//         }
//     }

//     /** XXX **/

//     void "test courseGet: Invalid access_token " () {

//     	when:
//     	prepareData()
//     	params.access_token = "ajdsfbks"
//     	controller.courseGet("ajdsfbks" , course1.id.toString())

//     	then:
//     	if (view != '/failure') {
//     		println "courseGet()-Invalid access_token: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}

//     }

//     void "test courseGet: Null access_token " () {

//     	when:
//     	prepareData()
//     	controller.courseGet(null , course1.id.toString())

//     	then:
//     	if (view != '/failure') {
//     		println "courseGet()-Null access_token: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }


//     void "test courseGet: Invalid course_id " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	controller.courseGet("inst-1000" , "basdfjkva")

//     	then:
//     	if (view != '/failure') {
//     		println "courseGet()-Invalid course_id: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
    	
//     }


//     /**
//      * Tests for: postCourse(String access_token, String crn, String name, String user_id) 
//      * Endpoint to POST a new course to the server
//      * @param access_token - The access token of the requesting user
//      * @param crn - the id of the course being added
//      * @param name - the name of the course being added
//      * @param user_id - the user id of the instructor the course will be added to
//      * ["access_token", "crn", "name"]
//      */

//      // As admin
//     void "test postCourse(): Valid parameters (Admin)" () {

//     	when:
    	
//     	prepareData()
//     	params.access_token = "admin-1000"
//     	params.crn = "99999"
//     	params.name = "CSC101"
//     	controller.postCourse("admin-1000" , "99999", "CSC101", Long.toString(admin.id))

//     	then:

//     	def course = model.course

//     	if (course.name != "CSC101"){
//     		println("postCourse()-Valid parameters: New course not returned")
//     		assert false
//     	}

//     }

//     // As instructor
//     void "test postCourse(): Valid parameters (Instructor)" () {

//     	when:
    	
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.crn = "45678"
//     	params.name = "CSC101"
//     	controller.postCourse("inst-1000" , "45678", "CSC101", Long.toString(inst1.id))

//     	then:

//     	println "postCourse(): Valid parameters (Instructor) model: $model"

//     	def course = model.course

//     	if (course.name != "CSC101"){
//     		println("postCourse()-Valid parameters: New course not returned")
//     		assert false
//     	}

//     }

// //     /** XXX **/

//     void "test postCourse(): Invalid access_token " () {

//     	when:
//     	prepareData()
//     	params.access_token = "Invalid"
//     	params.crn = "12345"
//     	params.name = "CSC101"
//     	controller.postCourse("Invalid" , "12345", "CSC101", Long.toString(inst1.id))

//     	then:

//     	if (view != '/failure') {
//     		println "postCourse()-Invalid access_token: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
    	
//     }

//     void "test postCourse(): null access_token " () {

//     	when:
//     	prepareData()
//     	params.crn = "12345"
//     	params.name = "CSC101"
//     	controller.postCourse(null, "12345", "CSC101", Long.toString(inst1.id))

//     	then:
//     	if (view != '/failure') {
//     		println "postCourse()-null access_token: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
    	
//     }

//     void "test postCourse(): Invalid crn " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.crn = "ABCDEF"
//     	params.name = "CSC101"
//     	controller.postCourse("inst-1000" , "ABCDEF", "CSC101", Long.toString(inst1.id))

//     	then:
//     	if (view != '/failure') {
//     		println "postCourse()-Invalid crn: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test postCourse(): null crn " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.name = "CSC101"
//     	controller.postCourse("inst-1000" , null, "CSC101", Long.toString(inst1.id))

//     	then:
//     	if (view != '/failure') {
//     		println "postCourse()-null crn: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test postCourse(): Invalid name " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.crn = "12345"
//     	params.name = "sdf dfknjds fv "
//     	controller.postCourse("inst-1000" , "sdf dfknjds fv ", "CSC101", Long.toString(inst1.id))

//     	then:
//     	if (view != '/failure') {
//     		println "postCourse()-Invalid name : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test postCourse(): null name " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.crn = "12345"
//     	controller.postCourse("inst-1000" , "12345", null, Long.toString(inst1.id))

//     	then:
//     	if (view != '/failure') {
//     		println "postCourse()-null name: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test postCourse(): Invalid user_id " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.crn = "12345"
//     	params.name = "CSC101"
//     	controller.postCourse("inst-1000" , "12345", "CSC101", "sda")

//     	then:
//     	if (view != '/failure') {
//     		println "postCourse()-Invalid user_id: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test postCourse(): null user_id " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.crn = "12345"
//     	params.name = "CSC101"
//     	controller.postCourse("inst-1000" , "12345", "CSC101", null)

//     	then:
//     	if (view != '/failure') {
//     		println "postCourse()-null user_id: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}

//     }


// //     /**
// //      * Test for: deleteCourse(String access_token, String course_id)
// //      * Endpoint to perform delete operation active courses.
// //      * @param access_token - The access token of the requesting user.
// //      * @param course_id - The id of the course.
// // 		* ["access_token", "course_id"]
// //      */

//      void "test deleteCourse(): Valid parameters " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = course1.id
//     	controller.deleteCourse("inst-1000" , Long.toString(course1.id))

//     	then:
    	
//     	if (model.token.getAccessToken() != "inst-1000") {
//     		println "Incorrect token: $model.token.getAccessToken()"
//     		assert false
//     	}
//     }

// //     /** XXX **/

//     void "test deleteCourse(): Invalid access_token " () {

//     	when:
//     	prepareData()
//     	params.access_token = "djskdn"
//     	params.course_id = course1.id
//     	controller.deleteCourse("djskdn" , course1.id.toString())

//     	then:
//     	if (view != '/failure') {
//     		println "deleteCourse()-null user_id: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test deleteCourse(): null access_token " () {

//     	when:
//     	prepareData()
//     	params.course_id = course1.id
//     	controller.deleteCourse(null , Long.toString(course1.id))

//     	then:
//     	if (view != '/failure') {
//     		println "deleteCourse()-null access_token: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test deleteCourse(): Invalid course_id " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = "Invalid"
//     	controller.deleteCourse("inst-1000" , "Invalid")

//     	then:
//     	if (view != '/failure') {
//     		println "deleteCourse()-Invalid course_id : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test deleteCourse(): null course_id " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	controller.deleteCourse("inst-1000" , null)

//     	then:
//     	if (view != '/failure') {
//     		println "deleteCourse()-null course_id: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }


//      /**
//      * Test for: getCourseStudent(String access_token, String course_id) 
//      * Endpoint to get a list of students in a specified course.
//      * @param access_token - The access token of the requesting user.
//      * @param course_id - The id of the course
//      */

//      void "test getCourseStudent(): Valid parameters " () {

//     	when:
    	
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = course1.id
//     	controller.getCourseStudent("inst-1000" , Long.toString(course1.id))

//     	then:
    	
//     	def students = model.students
//     	def count = 0

//     	for(student in students) {
//             switch(student.getEmail()) {
//                 case b.getEmail():
//                     count++
//                     break
//                 case a.getEmail():
//                     count++
//                     break
//                 default:
//                     println("Found student that was not supposed to exist: $student")
//                     assert false 
//                     break
//             }
//         }

//         if(count != 2) {
//             println("Did not find all expected emails, found: $students")
//             assert false
//         }
//     }

//     // /** XXX **/

//     void "test getCourseStudent(): Invalid access_token " () {

//     	when:
//     	prepareData()
//     	params.access_token = "Invalid"
//     	params.course_id = course1.id
//     	controller.getCourseStudent("Invalid" , Long.toString(course1.id))

//     	then:
//     	if (view != '/failure') {
//     		println "getCourseStudent()-Invalid access_token : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test getCourseStudent(): null access_token " () {

//     	when:
//     	prepareData()
//     	params.course_id = course1.id
//     	controller.getCourseStudent(null , Long.toString(course1.id))

//     	then:
//     	if (view != '/failure') {
//     		println "getCourseStudent()-null access_token: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test getCourseStudent(): Invalid course_id " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = course1.id
//     	controller.getCourseStudent("inst-1000" , Long.toString(course1.id))

//     	then:
//     	if (view != '/failure') {
//     		println "getCourseStudent()-Invalid course_id  : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test getCourseStudent(): null course_id " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	controller.getCourseStudent("inst-1000" , null)

//     	then:
//     	if (view != '/failure') {
//     		println "getCourseStudent()-null course_id : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }


//     /**
//      * Test for: postCourseStudent(String access_token, String course_id, String email) 
//      * Endpoint to add students to an existing course by their email address. The POST request can also take a CSV file
//      * containing student emails. If this CSV file is included in the request then it will be parsed and the students
//      * associated with each of the emails in the file will be added to the course.
//      * @param access_token - The access token of the requesting user
//      * @param course_id - the id of the course being added
//      * @param email - the name of an email address by which to add a student
//      * ["access_token", "course_id"]
//      */
     
// 	void "test postCourseStudent(): Valid parameters " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = course1.id
//     	controller.postCourseStudent("inst-1000" , Long.toString(course1.id), a.getEmail())

//     	then:
//     	def students = model.students
//     	def count = 0 

//     	for(student in students) {
//             switch(student.getEmail()) {
//                 case a.getEmail():
//                     count++
//                     break
//                 default:
//                     println("Found student that was not supposed to exist: $student")
//                     assert false 
//                     break
//             }
//         }

//         if(count != 1) {
//             println("Did not find all expected emails, found: $students")
//             assert false
//         }

//     }

//     /** XXX **/

//     void "test postCourseStudent(): Invalid access_token " () {

//     	when:
//     	prepareData()
//     	params.access_token = "Invalid"
//     	params.course_id = course1.id
//     	controller.postCourseStudent("Invalid" , Long.toString(course1.id), a.getEmail())

//     	then:
//     	if (view != '/failure') {
//     		println "postCourseStudent()- Invalid access_token: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test postCourseStudent(): null access_token " () {

//     	when:
//     	prepareData()
//     	params.course_id = course1.id
//     	controller.postCourseStudent(null , Long.toString(course1.id), a.getEmail())

//     	then:
//     	if (view != '/failure') {
//     		println "postCourseStudent()- null access_token: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test postCourseStudent(): Invalid course_id " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = "Invalid"
//     	controller.postCourseStudent("inst-1000" , "Invalid", a.getEmail())

//     	then:
//     	if (view != '/failure') {
//     		println "postCourseStudent()- Invalid course_id: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test postCourseStudent(): null course_id " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	controller.postCourseStudent("inst-1000" , null, a.getEmail())

//     	then:
//     	if (view != '/failure') {
//     		println "postCourseStudent()- : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test postCourseStudent(): Invalid email " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = course1.id
//     	controller.postCourseStudent("inst-1000" , Long.toString(course1.id), "Invalid")

//     	then:
//     	if (view != '/failure') {
//     		println "postCourseStudent()- Invalid email: Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test postCourseStudent(): null email " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = course1.id
//     	controller.postCourseStudent("inst-1000" , Long.toString(course1.id), null)

//     	then:
//     	if (view != '/failure') {
//     		println "postCourseStudent()-  null email : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }


//     /**
//      * Test for: deleteCourseStudent(String access_token, String course_id, String user_id)
//      * @param access_token - The access token of the requesting user
//      * @param course_id - the id of the course being added
//      * @param user_id - the user id of the instructor the course will be added to
//      * ["access_token", "course_id", "user_id"]
//      */

//      void "test deleteCourseStudent(): Valid parameters " () {

//     	when:
    	
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = Long.toString(course1.id)
//     	params.user_id = Long.toString(inst1.id)
//     	controller.deleteCourseStudent("inst-1000" , Long.toString(course1.id), Long.toString(inst1.id))

//     	then:

//     	if (model.token.getAccessToken() != "inst-1000") {
//     		println "deleteCourseStudent()- Valid parameters: Incorrect token: $model.token.getAccessToken()"
//     		assert false
//     	}

//     }

//     /** XXX **/

//     void "test deleteCourseStudent(): Invalid access_token " () {

//     	when:

//     	prepareData()
//     	params.access_token = "Invalid"
//     	params.course_id = Long.toString(course1.id)
//     	params.user_id = Long.toString(inst1.id)
//     	controller.deleteCourseStudent("Invalid" , Long.toString(course1.id), Long.toString(inst1.id))

//     	then:

//     	if (view != '/failure') {
//     		println "deleteCourseStudent()- Invalid access_token  : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test deleteCourseStudent(): null access_token " () {

//     	when:

//     	prepareData()
//     	params.course_id = Long.toString(course1.id)
//     	params.user_id = Long.toString(inst1.id)
//     	controller.deleteCourseStudent(null, Long.toString(course1.id), Long.toString(inst1.id))

//     	then:

//     	if (view != '/failure') {
//     		println "deleteCourseStudent()-  null access_token : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test deleteCourseStudent(): Invalid course_id " () {

//     	when:

//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = "Invalid"
//     	params.user_id = Long.toString(inst1.id)
//     	controller.deleteCourseStudent("inst-1000" , "Invalid", Long.toString(inst1.id))

//     	then:

//     	if (view != '/failure') {
//     		println "deleteCourseStudent()- Invalid course_id : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test deleteCourseStudent(): null course_id " () {

//     	when:

//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.user_id = Long.toString(inst1.id)
//     	controller.deleteCourseStudent("inst-1000" , null, Long.toString(inst1.id))

//     	then:

//     	if (view != '/failure') {
//     		println "deleteCourseStudent()-  null course_id : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test deleteCourseStudent(): Invalid user_id " () {

//     	when:

//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = Long.toString(course1.id)
//     	params.user_id = 21314324
//     	controller.deleteCourseStudent("inst-1000" , Long.toString(course1.id),"Invalid")

//     	then:

//     	if (view != '/failure') {
//     		println "deleteCourseStudent()-  Invalid user_id : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test deleteCourseStudent(): null user_id " () {

//     	when:

//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = Long.toString(course1.id)
//     	controller.deleteCourseStudent("inst-1000" , Long.toString(course1.id), null)

//     	then:

//     	if (view != '/failure') {
//     		println "deleteCourseStudent()-  null user_id : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     /**
//      * getAttendance(String access_token, String course_id, String student_id, String date, String start_date, String end_date)
//      * Gets the attendance for a selected course and date or for a selected course, student, and range of dates
//      * @param access_token - the access_token of the user
//      * @param course_id - the course id of the course being selected
//      * @param student_id - the student's id of the student being selected
//      * @param date - the single date selected for a course
//      * @param start_date - the start date of the range of dates selected
//      * @param end_date - the end date of the range of dates selected
//      * @return - returns a json view
//      */
//      void "test getAttendance(): Valid parameters (Single Date) " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = Long.toString(course1.id)
//     	params.date =  "04-06-17"
//     	controller.getAttendance("inst-1000", Long.toString(course1.id), Long.toString(a.id)
// 		, "04-06-17", null, null)

//     	then:
    	
//     	if(model.attendees != null){

// 	    	for(student in model.attendees) {
// 	            switch(student.getEmail()) {
// 	                case a.getEmail():
// 	                    count++
// 	                    break
// 	                default:
// 	                    println("Found student that was not supposed to exist: $student")
// 	                    assert false 
// 	                    break
// 	            }
// 	       	}

// 		    if(count != 1) {
// 		            println("Did not find all expected emails, found: $students")
// 		            assert false
// 		    }
    
//     	}
//     }

//     void "test getAttendance(): Valid parameters (Double Date) " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = Long.toString(course1.id)
//     	params.student_id = Long.toString(a.id)
//     	params.start_date = "04-06-17"
//     	params.end_date =  "04-07-17"
//     	controller.getAttendance("inst-1000", Long.toString(course1.id), Long.toString(a.id)
// 		, null, "04-06-17", "04-07-17")

//     	then:
//     	def count = 0
//     	if(model.attendees != null){

// 	    	for(student in model.attendees) {
// 	            switch(student.getEmail()) {
// 	                case a.getEmail():
// 	                    count++
// 	                    break
// 	                default:
// 	                    println("Found student that was not supposed to exist: $student")
// 	                    assert false 
// 	                    break
// 	            }
// 	       	}

// 		    if(count != 1) {
// 		            println("Did not find all expected emails, found: $model.attendees")
// 		            assert false
// 		    }
    
//     	} else {
//     		println "No attendees, found $model.attendees"
//     		assert false
//     	}
//     }

//     /** XXX **/

//     void "test getAttendance(): Invalid access_token " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = Long.toString(course1.id)
//     	params.date =  "04-06-17"
//     	controller.getAttendance("inst-1000", Long.toString(course1.id), Long.toString(a.id)
// 		, "04-06-17", null, null)
    	
//     	then:
//     	if (view != '/failure') {
//     		println "getAttendance()-  : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test getAttendance(): null access_token " () {

//     	when:
//     	prepareData()
//     	params.course_id = Long.toString(course1.id)
//     	params.date =  "04-06-17"
//     	controller.getAttendance("inst-1000", Long.toString(course1.id), Long.toString(a.id)
// 		, "04-06-17", null, null)
    	
//     	then:
//     	if (view != '/failure') {
//     		println "getAttendance()-  : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test getAttendance(): Invalid course_id " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = Long.toString(course1.id)
//     	params.date =  "04-06-17"
//     	controller.getAttendance("inst-1000", Long.toString(course1.id), Long.toString(a.id)
// 		, "04-06-17", null, null)
    	
//     	then:
//     	if (view != '/failure') {
//     		println "getAttendance()-  : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test getAttendance(): null course_id " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = Long.toString(course1.id)
//     	params.date =  "04-06-17"
//     	controller.getAttendance("inst-1000", Long.toString(course1.id), Long.toString(a.id)
// 		, "04-06-17", null, null)
    	
//     	then:
//     	if (view != '/failure') {
//     		println "getAttendance()-  : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test getAttendance(): Invalid student_id " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = Long.toString(course1.id)
//     	params.date =  "04-06-17"
//     	controller.getAttendance("inst-1000", Long.toString(course1.id), Long.toString(a.id)
// 		, "04-06-17", null, null)
    	
//     	then:
//     	if (view != '/failure') {
//     		println "getAttendance()-  : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test getAttendance(): null student_id " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = Long.toString(course1.id)
//     	params.date =  "04-06-17"
//     	controller.getAttendance("inst-1000", Long.toString(course1.id), Long.toString(a.id)
// 		, "04-06-17", null, null)
    	
//     	then:
//     	if (view != '/failure') {
//     		println "getAttendance()-  : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }

//     void "test getAttendance(): Invalid date " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = Long.toString(course1.id)
//     	params.date =  "Invalid"
//     	controller.getAttendance("inst-1000", Long.toString(course1.id), Long.toString(a.id)
// 		, "Invalid", null, null)
    	
//     	then:
//     	if (view != '/failure') {
//     		println "getAttendance()- Invalid date : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }



//     void "test getAttendance(): Invalid start_date " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = Long.toString(course1.id)
//     	params.student_id = Long.toString(a.id)
//     	params.start_date = "Invalid"
//     	params.end_date =  "04-07-17"
//     	controller.getAttendance("inst-1000", Long.toString(course1.id), Long.toString(a.id)
// 		, null, "Invalid", "04-07-17")

//     	then:
//     	if (view != '/failure') {
//     		println "getAttendance()- Invalid date : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }


//     void "test getAttendance(): Invalid end_date " () {

//     	when:
//     	prepareData()
//     	params.access_token = "inst-1000"
//     	params.course_id = Long.toString(course1.id)
//     	params.student_id = Long.toString(a.id)
//     	params.start_date = "04-06-17"
//     	params.end_date =  "Invalid"
//     	controller.getAttendance("inst-1000", Long.toString(course1.id), Long.toString(a.id)
// 		, null, "04-06-17", "Invalid")

//     	then:
//     	if (view != '/failure') {
//     		println "getAttendance()- Invalid date : Expected view to be '/failure' actual: $view"
//     		assert false 
//     	}
//     }


}

