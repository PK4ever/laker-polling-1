package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.services.CourseListParserService
import edu.oswego.cs.lakerpolling.services.CourseService
import edu.oswego.cs.lakerpolling.services.PreconditionService
import edu.oswego.cs.lakerpolling.util.QueryResult
import org.springframework.web.multipart.MultipartFile
import org.springframework.http.HttpStatus

class CourseController {

    static responseFormats = ['json', 'xml']

    PreconditionService preconditionService
    CourseService courseService
    CourseListParserService courseListParserService

    /**
     * Endpoint to GET a course or list of courses
     * @param access_token - the access token of the requesting user
     * @param course_id - only needed when searching for a specific course. otherwise input as null
     */
    def courseGet(String access_token, String course_id) {
        def require = preconditionService.notNull(params, ["access_token"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            QueryResult<List<Course>> result = course_id == null ?
                    courseService.getAllCourses(token)
                    : courseService.getAllCourses(token, course_id)
            if (result.success) {
                render(view: 'courseList', model: [token: token, courses: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     * Endpoint to POST a new course to the server
     * @param access_token - The access token of the requesting user
     * @param crn - the id of the course being added
     * @param name - the name of the course being added
     * @param user_id - the user id of the instructor the course will be added to
     */
    def postCourse(String access_token, String crn, String name, String user_id) {
        def require = preconditionService.notNull(params, ["access_token", "crn", "name"])
        def token = preconditionService.accessToken(access_token, require).data

        if (require.success) {
            def adminCreate = preconditionService.notNull(params, ["user_id"])
            def result
            if (adminCreate.success) {
                result = courseService.adminCreateCourse(token, crn, name, user_id)
            } else {
                result = courseService.instructorCreateCourse(token, crn, name)
            }

            if (result.success) {
                render(view: 'newCourse', model: [course: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }

    }

    /**
     * Endpoint to perform delete operation active courses.
     * @param access_token - The access token of the requesting user.
     * @param course_id - The id of the course.
     */
    def deleteCourse(String access_token, String course_id) {
        def require = preconditionService.notNull(params, ["access_token", "course_id"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            def result = courseService.deleteCourse(token, course_id)
            if (result.success) {
                render(view: 'deleteResult', model: [token: token])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     * Endpoint to get a list of students in a specified course.
     * @param access_token - The access token of the requesting user.
     * @param course_id - The id of the course
     */

    def getCourseStudent(String access_token, String course_id) {
        def require = preconditionService.notNull(params, ["access_token", "course_id"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            def results = courseService.getAllStudents(token, course_id)
            if (results.success) {
                render(view: 'studentList', model: [token: token, courseID: course_id.toLong(), students: results.data])
            } else {
                render(view: '../failure', model: [errorCode: results.errorCode, message: results.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    /**
     * Endpoint to add students to an existing course by their email address. The POST request can also take a CSV file
     * containing student emails. If this CSV file is included in the request then it will be parsed and the students
     * associated with each of the emails in the file will be added to the course.
     * @param access_token - The access token of the requesting user
     * @param course_id - the id of the course being added
     * @param email - the name of an email address by which to add a student
     * @param user_id - the user id of the instructor the course will be added to
     */
    def postCourseStudent(String access_token, String course_id, String email) {
        def require = preconditionService.notNull(params, ["access_token", "course_id"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            List<String> emails = new ArrayList<>()

            if (params.containsKey("file")) {
                MultipartFile file = request.getFile("file")
                if (file != null) {
                    QueryResult<List<String>> parseResult = courseListParserService.parse(file)
                    if (parseResult.success) {
                        emails = parseResult.data
                    } else {
                        render(view: '../failure', model: [errorCode: parseResult.errorCode, message: parseResult.message])
                        return
                    }
                }
            }

            if (email != null) {
                emails.add(email)
            }

            def result = courseService.postStudentsToCourse(token, course_id, emails)
            if (result.success) {
                render(view: 'studentList', model: [token: token, courseID: course_id.toLong(), students: result.data])
            } else {
                render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def deleteCourseStudent(String access_token, String course_id, String user_id) {
        def require = preconditionService.notNull(params, ["access_token", "course_id", "user_id"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            if (course_id.isLong()) {
                List<String> userIds = user_id.indexOf(",") != -1 ? user_id.split(",").toList() : [user_id]
                def result = courseService.deleteStudentCourse(token, course_id.toLong(), userIds)
                if (result.success) {
                    render(view: 'deleteResult', model: [token: token])
                } else {
                    render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
                }
            } else {
                def bad = QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
                render(view: '../failure', model: [errorCode: bad.errorCode, message: bad.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }

    }

    def getAttendance(String access_token, String course_id, String student_id, String date, String start_date, String end_date) {
        def require = preconditionService.notNull(params, ["access_token", "course_id"])
        def token = preconditionService.accessToken(access_token).data

        if (require.success) {
            if (preconditionService.notNull(params, ["date"]).success) {
                List<String> revisedDate = date.indexOf('-') != -1 ? date.split("-").toList() : null
                String reDate = revisedDate.get(1) + "/" + revisedDate.get(2) + "/" + revisedDate.get(0)
                def students = courseService.getAllStudentAttendance(course_id, reDate)
                if (students.success) {
                    render(view: 'attendanceList', model: [token: token, attendees: students.data])
                } else {
                    render(view: '../failure', model: [errorCode: students.errorCode, message: students.message])
                }
            } else if (preconditionService.notNull(params, ["student_id", "start_date", "end_date"]).success) {
                List<String> revisedStart = start_date.indexOf('-') != -1 ? start_date.split("-").toList() : null
                List<String> revisedEnd = end_date.indexOf('-') != -1 ? end_date.split("-").toList() : null
                String start = revisedStart.get(1) + "/" + revisedStart.get(2) + "/" + revisedStart.get(0)
                String end = revisedEnd.get(1) + "/" + revisedEnd.get(2) + "/" + revisedEnd.get(0)
                println("START: " + start + " END: " + end)
                def student = courseService.getStudentAttendance(student_id, start, end)
                if (student.success) {
                    render(view: 'attendanceList', model: [token: token, attendees: student.data])
                } else {
                    render(view: '../failure', model: [errorCode: student.errorCode, message: student.message])
                }
            } else {
                render(view: '../failure', model: [errorCode: HttpStatus.BAD_REQUEST.value(), message: "Missing parameter"])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }

    }

}
