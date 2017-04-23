package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.domains.Attendance
import edu.oswego.cs.lakerpolling.domains.Attendee
import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.QueryResult
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse
import java.text.SimpleDateFormat

@Transactional
class AttendanceService {

    CourseService courseService

    /**
     * gets the attendance for a selected student during the range of dates (inclusive) provided for the selected course
     * @param studentID - the ID of the selected student
     * @param courseID - the ID of the selected course
     * @param dateRange - a range of dates to filter the attendance result by
     * @return - returns a QueryResult containing the list of Attendee objects related to the student and date range
     */
    QueryResult<List<Attendee>> getStudentAttendance(AuthToken token, String studentID, String courseID, Range<Date> dateRange) {
        User requestingUser = token.user
        User student = User.findById(studentID.toLong())
        Course course = Course.findById(courseID.toLong())

        if (!course || !student) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        } else if (!courseService.hasInstructorAccess(requestingUser, course)) {
            return QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
        }

        QueryResult result = new QueryResult()
        result.data = Attendee.where {
            student == student && attendance { course == course && date >= dateRange.from && date <= dateRange.to }
        }.list()
        result
    }

    /**
     * gets the attendance for all students in a selected course during the range of dates (inclusive) provided for the selected course
     * @param courseID - the ID of the selected course
     * @param dateRange - a range of dates to filter the attendance result by
     * @return - returns a QueryResult containing a list of all attendees related to the course and date range
     */
    QueryResult<List<Attendance>> getAllStudentAttendance(AuthToken token, String courseID, Range<Date> dateRange) {
        User requestingUser = token.user
        Course course = Course.findById(courseID.toLong())

        if (!course) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        } else if (!courseService.hasInstructorAccess(requestingUser, course)) {
            return QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
        }

        QueryResult result = new QueryResult()
        result.data = Attendance.where {
            course == course && date >= dateRange.from && date <= dateRange.to
        }.list()
        result
    }

    /**
     * Outputs a csv file through the response if pre checks pass through.
     * @param token - The token to identify the user making the request.
     * @param courseId - The id of the course to get data for.
     * @param response - The response object to send data through.
     * @return A query result with success true or false based on the operation.
     */
    QueryResult getCourseAttendanceCsv(AuthToken token, long courseId, HttpServletResponse response) {
        QueryResult result = new QueryResult(success: true)
        Course course = Course.findById(courseId)

        if (courseService.hasInstructorAccess(token.user, course)) {

            ServletOutputStream outputStream = response.outputStream
            List<Attendance> attendances = Attendance.createCriteria().list {
                eq('course', course)

                lt("date", new Date())
                order("date", "asc")
            } as List<Attendance>

            Set<User> students = course.students
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy")
            SimpleDateFormat fn = new SimpleDateFormat("MM-dd-yy")
            response.setHeader("Content-disposition",
                    "filename=attendance-${course.name}_${fn.format(attendances.first().date)}" +
                            "___${fn.format(attendances.last().date)}.csv")
            response.contentType = "text/csv"
            response.characterEncoding = "UTF-8"

            outputStream << "Name"
            outputStream << ",Email"
            attendances.each {
                outputStream << ",${formatter.format(it.date)}"
            }
            outputStream << "\n"
            outputStream.flush()

            students.eachWithIndex { student, index ->
                outputStream << "${student.firstName} ${student.lastName}"
                outputStream << ",${student.email}"
                attendances.each { attendance ->
                    Attendee attendee = Attendee.findByAttendanceAndStudent(attendance, student)
                    outputStream << ",${attendee.attended ? "v" : "x"}"
                }
                if (index < students.size() - 1) {
                    outputStream << "\n"
                }
                outputStream.flush()
            }
        } else {
            result.success = false
            result.errorCode = HttpStatus.UNAUTHORIZED.value()
            result.message = HttpStatus.UNAUTHORIZED.reasonPhrase
        }

        result
    }

}
