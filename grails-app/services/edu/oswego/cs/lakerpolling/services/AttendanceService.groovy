package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.domains.Attendance
import edu.oswego.cs.lakerpolling.domains.Attendee
import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.QueryResult
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

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



}
