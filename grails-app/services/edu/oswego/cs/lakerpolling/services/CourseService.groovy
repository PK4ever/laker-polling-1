package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.domains.Attendance
import edu.oswego.cs.lakerpolling.domains.Attendee
import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.domains.Quiz
import edu.oswego.cs.lakerpolling.domains.QuizSubmission
import edu.oswego.cs.lakerpolling.domains.Role
import edu.oswego.cs.lakerpolling.domains.User
import edu.oswego.cs.lakerpolling.util.QueryResult
import edu.oswego.cs.lakerpolling.util.RoleType
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

/**
 * Service to perform transactional operations relating to {@link Course} model.
 */
@Transactional
class CourseService {
    UserService userService

    /**
     * Lists students in a specified course
     * @param token - The token to use to retrieve the course-list.
     * @param courseId - The id of the course from which to list students.
     * @return The results of the operations.
     */
    QueryResult<List<User>> getAllStudents(AuthToken token, String courseId) {
        QueryResult<List<User>> res = new QueryResult<>()
        User requestingUser = token?.user
        long cid = courseId.isLong() ? courseId.toLong() : -1

        if (requestingUser != null && isInstructorOrAdmin(requestingUser.role) && cid != -1) {
            Course course = Course.findById(cid)
            if (course != null) {

                // if this is an admin performing the action
                if (requestingUser.role.type == RoleType.ADMIN) {
                    res.data = course.students
                } else {
                    // make sure the requesting user is the instructor
                    if (isInstructorOf(requestingUser, course)) {
                        res.data = course.students
                    } else {
                        QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, res)
                    }
                }
            } else {
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, res)
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, res)
        }
        return res
    }

    /**
     * Deletes a specified course. The role of the requesting user is taken into consideration. Only admin and
     * instructors can delete courses and instructors can only delete their own courses.
     * @param token - The token to use to retrieve the requesting user.
     * @param courseId - The id of the course to delete.
     * @return The results of the operations.
     */
    QueryResult<Course> deleteCourse(AuthToken token, String courseId) {
        QueryResult<Course> res = new QueryResult<>()
        User requestingUser = token?.user
        long cid = courseId.isLong() ? courseId.toLong() : -1

        if (requestingUser != null && isInstructorOrAdmin(requestingUser.role) && cid != -1) {
            Course course = Course.findById(cid)
            if (course != null) {
                // if this is an admin performing the action
                if (requestingUser.role.type == RoleType.ADMIN) {
                    doDelete(course, res)
                } else {
                    //make sure the requesting user is the instructor
                    if (isInstructorOf(requestingUser, course)) {
                        doDelete(course, res)
                    } else {
                        QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, res)
                    }
                }
            } else {
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, res)
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, res)
        }

        res
    }

    /**
     * Adds the users associated with the each of the emails in the given list of emails to the course with the
     * specified courseId. Only admin and instructors can only add students and instructors can only add students to
     * their own courses. If a user with the one of the emails does not already exist then a placeholder account is
     * created for that user.
     * @param token - The token to use to retrieve the requesting user.
     * @param courseId - The id of the course to delete.
     * @param emails - A list of emails to add to the course
     * @return The results of the operations.
     */
    QueryResult<List<User>> postStudentsToCourse(AuthToken token, String courseId, List<String> emails) {
        QueryResult<List<User>> result = new QueryResult<>()
        User requestingUser = token?.user
        long cid = courseId.isLong() ? courseId.toLong() : -1

        if (requestingUser != null && cid != -1) {
            Course course = Course.findById(cid)
            if (course != null) {
                if (hasInstructorAccess(requestingUser, course)) {
                    List<User> users = new ArrayList<>()

                    for (email in emails) {
                        if(email.indexOf("oswego.edu") == -1) {
                            QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
                        }
                    }

                    for (email in emails) {
                            User user = userService.getOrMakeByEmail(email)
                            course.addToStudents(user)
                            users.add(user)

                    }
                    result.data = users
                } else {
                    QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
                }
            } else {
                QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, result)
        }

        result
    }

    /**
     * Removes a list of students from a given course. The request is allowed if the requesting user's role
     * is ADMIN or is instructor of the course.
     * @param token - The token identifying the requesting user.
     * @param courseId - The id of the course to delete from.
     * @param userIds - The list of user ids to remove.
     * @return A query result object.
     */
    QueryResult deleteStudentCourse(AuthToken token, long courseId, List<String> userIds) {
        QueryResult res = new QueryResult()
        User requestingUser = token?.user
        Course course = Course.findById(courseId)

        if (course != null) {
            // user and course must exist. check if role is admin or is instructor of course
            if (requestingUser != null && (requestingUser.role.type == RoleType.ADMIN
                    || isInstructorOf(requestingUser, course))) {
                try {
                    userIds.each { id ->
                        course.removeFromStudents(User.get(id as Long))
                    }
                } catch (Exception e) {
                    e.printStackTrace()
                    QueryResult.fromHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR, res)
                }
            } else {
                QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, res)
            }
        } else {
            QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, res)
        }

        res
    }

    /**
     * Creates a course for an instructor
     * @param token - The AuthToken of the instructor
     * @param crn - The crn of the course being created
     * @param name - The name of the course to be created
     * @param result - Optional result to store data in
     * @return query results
     */
    QueryResult<Course> instructorCreateCourse(AuthToken token, String crn, String name, QueryResult<Course> result = new QueryResult<>(success: true)) {
        User instructor = token?.user
        if (isInstructorOrAdmin(instructor.role)) {
            result = createCourse(instructor, name, crn, result)
        } else {
            QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
        }
        result
    }

    /**
     * Creates a course for an instructor as an admin
     * @param token - The AuthToken of the admin
     * @param crn - The crn of the course being created
     * @param name - The name of the course being created
     * @param instructor - The instructor who will own the course
     * @param result - Optional result to store data in
     * @return query results
     */
    QueryResult<Course> adminCreateCourse(AuthToken token, String crn, String name, String instructor, QueryResult<Course> result = new QueryResult<>(success: true)) {
        User admin = token?.user
        User inst = User.findById(Long.parseLong(instructor))
        if (admin && inst && admin.role.type == RoleType.ADMIN && inst.role.type == RoleType.INSTRUCTOR) {
            result = createCourse(inst, name, crn, result)
        } else {
            QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
        }
        result
    }

    /**
     * Creates a course
     * @param instructor - The instructor who will own a course
     * @param name - The name of the course
     * @param crn - The crn of the course
     * @param result - the QueryResult of the request
     * @return query results
     */
    private QueryResult<Course> createCourse(User instructor, String name, String crn, QueryResult<Course> result) {
        Course course = new Course(name: name, crn: crn, instructor: instructor)
        course.save(flush: true, failOnError: true)
        result.data = course
        result
    }

    /**
     * Deletes a course.
     * @param course - The course to delete.
     * @param result - Optional result to store data in.
     * @return query results.
     */
    private QueryResult<Course> doDelete(Course course, QueryResult<Course> result = new QueryResult<>(success: true)) {
        try {
            def quizzes = Quiz.findAllByCourse(course)
            for ( quiz in quizzes ) {
                def submissions = QuizSubmission.findAllByQuiz(quiz)
                for ( submission in submissions ) {
                    submission.delete(flush: true, failOnError: true)
                }
                quiz.delete(flush: true, failOnError: true)
            }

            def attendanceList = Attendance.findAllByCourse(course)
            for ( attendance in attendanceList ) {
                attendance.delete(flush: true, failOnError: true)
            }

            course.delete(flush: true, failOnError: true)
        } catch (Exception e) {
            e.printStackTrace()
            QueryResult.fromHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR, result)
        }
        result
    }

    /**
     * Identified if given role is an instructor or admin role.
     * @param role - The role to check.
     * @return True if conditions are met.
     */
    private boolean isInstructorOrAdmin(Role role) {
        role.type == RoleType.ADMIN || role.type == RoleType.INSTRUCTOR
    }

    /**
     * Removes a student from a course. Catching errors and returning results.
     * @param course - The course to delete from.
     * @param user - The user to remove.
     * @param result - The optional result to store data in.
     * @return A result object.
     */
    private QueryResult<Course> removeFromStudents(Course course, User user, QueryResult<Course> result = new QueryResult<>(success: true)) {
        try {
            course.removeFromStudents(user)
            course.save(flush: true, failOnError: true)
        } catch (Exception e) {
            e.printStackTrace()
            QueryResult.fromHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR, result)
        }
        result
    }

    void delete(int courseId) {
        Course course = Course.get(courseId)
        if (course != null) {
            course.delete()
        }
    }

    /**
     * Checks if the user has instructor access for the given course
     * @param user - the user to check for instructor access
     * @param course - the course to check
     * @return True if the user has instructor accss to the course
     */
    public boolean hasInstructorAccess(User user, Course course) {
        if (user.role.type == RoleType.ADMIN) {
            return true
        }
        return user.role.type == RoleType.INSTRUCTOR && isInstructorOf(user, course)
    }

    /**
     * Checks if the user has student access for the given course
     * @param user - the user to check for student access
     * @param course - the course to check
     * @return True if the user has student access to the course
     */
    public boolean hasStudentAccess(User user, Course course) {
        hasInstructorAccess(user, course) || isStudentOf(user, course)
    }

    /**
     * Checks if the user is an instructor of the course
     * @param user - the user to check
     * @param course - the course to check
     * @return true if the user is an instructor of the course
     */
    boolean isInstructorOf(User user, Course course) {
        user != null && course != null && course.instructorId == user.id
    }

    /**
     * Checks if the user is a student of the course
     * @param user - the user to check
     * @param course - the course to check
     * @return true if the user is a student of the course
     */
    boolean isStudentOf(User user, Course course) {
        course.students != null && course.students.find{x -> x.id == user.id}
    }

    /**
     * Lists all courses that are related to the user
     * @param token - The token to use to retrieve the requesting user.
     * @return The result of the operations
     */
    QueryResult<List<Course>> getAllCourses(AuthToken token) {
        QueryResult<List<Course>> res = new QueryResult<>()
        User requestingUser = token?.user
        List<Course> studentsCourses

        //checks to see if the user is an instructor
        if (requestingUser != null && requestingUser.role.type == RoleType.INSTRUCTOR) {
            res.data = Course.findAllByInstructor(requestingUser) // courses that are owned by the instructor
        } else if (requestingUser != null && requestingUser.role.type == RoleType.STUDENT) {
            // checks to see if the user is a student

            studentsCourses = Course.createCriteria().list {
                students {
                    eq ('id', requestingUser.id)
                }
            } as List<Course>

            res.data = studentsCourses // all the courses that the student belongs to
        } else if (requestingUser != null && requestingUser.role.type == RoleType.ADMIN) {
            // checks to see if the user is an admin
            res.data = Course.getAll() // all courses under the admin
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, res)
        }
        return res
    }

    /**
     * Gives the course with the requested course id
     * @param token - The auth token of the user
     * @param courseId - the id of the requested course
     * @return the result of the operations
     */
    QueryResult<List<Course>> getAllCourses(AuthToken token, String courseId) {
        QueryResult<List<Course>> res = new QueryResult<>()
        User requestingUser = token?.user

        long cid = courseId.isLong() ? courseId.toLong() : -1

        if (requestingUser != null && cid != -1) {
            Course course = Course.findById(cid)
            List<Course> selectedCourse = new ArrayList<>()
            selectedCourse.add(course)
            res.data = selectedCourse
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
        }
        return res
    }

    /**
     * Attempts to find the Course associated with the given ID String
     * @param courseIdString - A String representing a Course ID
     * @return A QueryResult containing the associated Course
     */
    QueryResult<Course> findCourse(String courseIdString) {
        QueryResult<Course> result = new QueryResult<>()
        if (!courseIdString.isLong()) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }
        Course course = Course.findById(courseIdString.toLong())
        if (!course) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }
        result.data = course
        result
    }

    /**
     * Returns a successfull Query Result if the user represented in token has student access to the given coursse
     * @param token - the Authtoken
     * @param course- the course
     * @return a QueryResult representing the result of the check
     */
    QueryResult verifyStudentAccess(AuthToken token, Course course) {
        def userResult = userService.findUser(token)
        if (!userResult.success) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }

        def user = userResult.data
        if (!hasStudentAccess(user, course)) {
            return QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
        }
        new QueryResult(success:true)
    }

    /**
     * Returns a successful Query Result if the user represented in token has instructor access to the given course
     * @param token - the Authtoken
     * @param course - the course
     * @return a QueryResult representing the result of the check
     */
    QueryResult verifyInstructorAccess(AuthToken token, Course course) {
        def userResult = userService.findUser(token)
        if (!userResult.success) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }

        def user = userResult.data
        if (!hasInstructorAccess(user, course)) {
            return QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
        }
        new QueryResult(success:true)
    }

}
