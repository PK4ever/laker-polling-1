package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.services.AttendanceService
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
    AttendanceService attendanceService
    CourseListParserService courseListParserService

    /**
     * Endpoint to GET a course or list of courses
     * @param access_token - the access token of the requesting user
     * @param course_id - only needed when searching for a specific course. otherwise input as null
     */
    def courseGet(String access_token, String course_id) {
        def require = preconditionService.notNull(params, ["access_token"])
        def token = preconditionService.accessToken(access_token, require).data

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
        def token = preconditionService.accessToken(access_token, require).data

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
        def token = preconditionService.accessToken(access_token, require).data

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
        def token = preconditionService.accessToken(access_token, require).data

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
        def token = preconditionService.accessToken(access_token, require).data

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

    /**
     * Gets the attendance for a selected course
     * @param access_token - the access_token of the user
     * @param course_id - the course id of the course being selected
     * @param student_id - the student id to filter the result by
     * @param date - the date to filter the result by
     * @param start_date - the start date of the range of dates to filter the results by
     * @param end_date - the end date of the range of dates to filter the results by
     * @return - returns a json view
     */
    def getAttendance(String access_token, String course_id, String student_id, String date, String start_date, String end_date) {
        def require = preconditionService.notNull(params, ["access_token", "course_id"])
        def token = preconditionService.accessToken(access_token, require).data

        if (require.success) {
            // dateRange spans either from start_date to end_date or just includes date
            def dateRange = getDateRange(date, start_date, end_date)
            if (dateRange) {
                if (student_id) {
                    def result = attendanceService.getStudentAttendance(token, student_id, course_id, dateRange)
                    if (result.success) {
                        render(view: 'getAttendees', model: [token: token, courseID: course_id.toLong(), attendees: result.data])
                    } else {
                        render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
                    }
                } else {
                    def result = attendanceService.getAllStudentAttendance(token, course_id, dateRange)
                    if (result.success) {
                        def attendanceList = result.data
                        if (attendanceList.size() == 1) {
                            def attendees = attendanceList.get(0).attendees
                            render(view: 'getAttendees', model: [token: token, courseID: course_id.toLong(), attendees: attendees])
                        } else {
                            render(view: 'getAttendanceList', model: [token: token, courseID: course_id.toLong(), attendanceList: result.data])
                        }
                    } else {
                        render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
                    }
                }
            } else {
                render(view: '../failure', model: [errorCode: 400, message: "Missing Date parameters"])
            }
        } else {
            render(view: '../failure', model: [errorCode: require.errorCode, message: require.message])
        }
    }

    def downloadAttendance(String access_token, String course_id) {
        QueryResult<AuthToken> checks = new QueryResult<>()
        preconditionService.notNull(params, ["access_token", "course_id"], checks)
        preconditionService.accessToken(access_token, checks)

        if (checks.success) {
            QueryResult<Long> convert = preconditionService.convertToLong(course_id, 'course_id')
            if (convert.success) {
                QueryResult result = attendanceService.getCourseAttendanceCsv(checks.data, convert.data, response)
                if(!result.success) {
                    render(view: '../failure', model: [errorCode: result.errorCode, message: result.message])
                }
            } else {
                render(view: '../failure', model: [errorCode: convert.errorCode, message: convert.message])
            }
        } else {
            render(view: '../failure', model: [errorCode: checks.errorCode, message: checks.message])
        }
    }

    Range<Date> getDateRange(String dateString, String startDateString, String endDateString) {
        if (dateString) {
            dateString = formatDateString(dateString)
            def date = new Date(dateString).clearTime()
            //create a range of dates including just the given date
            date..date
        } else if (startDateString && endDateString) {
            startDateString = formatDateString(startDateString)
            endDateString = formatDateString(endDateString)

            def startDate = new Date(startDateString).clearTime()
            def endDate = new Date(endDateString).clearTime()
            //create a range of dates from the given start date to the given end date
            startDate..endDate
        } else {
            null
        }
    }

    static String formatDateString(String date) {
        if (!date) {
            return null
        }
        List<String> dateList = date.indexOf('-') != -1 ? date.split("-").toList() : null
        dateList.get(1) + "/" + dateList.get(2) + "/" + dateList.get(0)
//        new Date(date)
    }

}
