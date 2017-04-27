package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.domains.*
import edu.oswego.cs.lakerpolling.util.QueryResult
import edu.oswego.cs.lakerpolling.util.QuestionType
import edu.oswego.cs.lakerpolling.util.RoleType
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

@Transactional
class QuestionService {
    CourseService courseService

    /**
     * creates a new question
     * @param token - the AuthToken of the user
     * @param question - (optional) a string representing the actual question
     * @param course_id - the id of the course
     * @param answers - a csv of booleans representing the answers
     * @return - returns a Question object
     */
    def createQuestion(AuthToken token, String question, String course_id, String answers) {
        def user = token.user
        List<String> tempList = answers.indexOf(",") != -1 ? answers.split(",").toList(): [answers]
        List<Boolean> answerList = new ArrayList<>()

        tempList.forEach {s ->
            if(s == "false") answerList.add(false)
            else if(s == "true") answerList.add(true)
            else return null
        }
        if (user) {
            if (user.role.type == RoleType.INSTRUCTOR) {
                Course course = Course.findById(course_id.toLong())
                if (course) {
                    Question newQuestion
                    if (question != null) {
                        newQuestion = new Question(course: course, answers: answerList, question: question, type: QuestionType.CLICKER)
                    } else {
                        newQuestion = new Question(course: course, answers: answerList, type: QuestionType.CLICKER)
                    }
                    newQuestion.active = false
                    newQuestion.save(flush: true, failOnError: true)
                    def attendance = Attendance.findByDateAndCourse(makeDate(), course)
                    if (attendance == null) {
                        attendance = new Attendance(date: makeDate(), course: course)
                        attendance.save(flush: true, failOnError: true)
                        course.students.each { s -> new Attendee(attended: false, attendance: attendance, student: s).save(flush: true, failOnError: true) }
                    }
                    newQuestion
                } else null
            } else null
        } else null
    }

    /**
     * answers a question
     * @param token - the AuthToken of the user
     * @param question_id - the id of the question being answered
     * @param answer - a csv of booleans
     * @return - returns a boolean indicating success or failure
     */
    def answerQuestion(AuthToken token, String question_id, String answer) {
        def user = token.user
        if(user) {
            if(user.role.getType() == RoleType.STUDENT) {
                List<String> tempList = answer.indexOf(",") != -1 ? answer.split(",").toList(): [answer]
                List<Boolean> answerList = new ArrayList<>()
                tempList.forEach {s ->
                    if(s == "false") answerList.add(false)
                    else if(s == "true") answerList.add(true)
                    else return false
                }
                def question = Question.findById(question_id.toLong())
                if(question) {
                    if(question.active) {
                        def attendee = Attendance.findByDateAndCourse(makeDate(), question.course).attendees.find { a -> a.student == user }
                        if (attendee) {
                            def isRight = questionResponseIsCorrect(answerList, question)
                            attendee.attended = true
                            def result = Answer.findByQuestionAndStudent(question, token.user)
                            if (result) {
                                result.correct = isRight
                                result.answers = answerList
                            } else {
                                result = new Answer(correct: isRight, question: question, student: token.user, answers: answerList)
                            }
                            result.save(flush: true, failOnError: true)
                            true
                        } else false
                    } else false
                } else false
            } else false
        } else false
    }

    boolean questionResponseIsCorrect(List<Boolean> response, Question question) {
        def answers = question.answers
        def isCorrect = true
        answers.eachWithIndex { a, i ->
            // isCorrect if it is not already marked as incorrect and the next response is also correct
            isCorrect = isCorrect && (a == response.get(i))
        }
        isCorrect
    }

    /**
     * flips a question from deactivated to active and vice versa
     * @param token - the AuthToken of the user
     * @param question_id - the id of the question
     * @param flipper - a boolean representing active or not
     * @return - returns a boolean indicating success or failure
     */
    def flipQuestion(AuthToken token, String question_id, boolean flipper) {
        def user = token.user
        if(user) {
            if(user.role.type == RoleType.INSTRUCTOR) {
                def question = Question.findById(question_id.toLong())
                if(question) {
                    question.active = flipper
                    true
                } else false
            } else false
        } else false
    }

    /**
     * gets any questions for a selected course
     * @param token -  the AuthToken of the user
     * @param course_id - the id of the selected course
     * @return - returns a collection of questions
     */
    def getQuestion(AuthToken token, String course_id) {
        def user = token.user
        if(user) {
            if(user.role.type == RoleType.STUDENT) {
                def course = Course.findById(course_id.toLong())
                if(course) {
                    def question = course.questions
                    if(question) question
                    else null
                } else null
            } else null
        } else null
    }

    /**
     * gets any active question for a selected course
     * @param token - the AuthToken of the user
     * @param course_id - the id of the course
     * @return - returns a collection of active questions
     */
    def getActiveQuestion(AuthToken token, String course_id) {
        def courseResult = courseService.findCourse(course_id)
        if(courseResult.success) {
            def course = courseResult.data
            if(courseService.verifyStudentAccess(token, course)) {
                def questions = course.questions
                if(questions) {
                    def active = questions.find{q -> q.active}
                    if(active) active
                    else null
                } else null
            } else null
        } else null
    }

    /**
     * Gets a summary of all of the student responses to the given question
     * @param token - the AuthToken of the user
     * @param questionIdString - A String representation of the id of the question
     * @return - returns a QueryResult containing the answer statistics
     */
    QueryResult<List<Integer>> getAnswers(AuthToken token, String questionIdString) {
        if (!questionIdString.isLong()) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }

        def questionId = questionIdString.toLong()
        def question = Question.findById(questionId)
        if (!question) {
            return QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
        }

        if (question.active) {
            def error = QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST)
            error.message = "Question is still enabled"
            return error
        }

        def course = question.course
        if (!courseService.verifyStudentAccess(token, course)) {
            return QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED)
        }

        def responses = Answer.where {
            question == question
        }.list()

        List<Integer> data = new ArrayList<>()
        question.answers.each {a -> data.add(0) }

        responses.each { r ->
            r.answers.eachWithIndex { answer, i ->
                if (answer) {
                    data.set(i, data.get(i) + 1)
                }
            }
        }

        def result = new QueryResult<>()
        result.data = data
        result
    }

    def getResults(AuthToken token, String date, String courseId) {
        def user = token.user
        def result = new QueryResult<List<Question>>()
        result.success = false

        if(user.role.type == RoleType.INSTRUCTOR || user.role.type == RoleType.ADMIN) {
            def course = Course.findById(courseId.toLong())
            if(course) {
                def qDate = makeDate(date)
                def questions = course.questions.findAll{q -> (q.type == QuestionType.CLICKER && isSameDay(q.dateCreated, qDate)) }
                questions.sort{a, b -> a.id <=> b.id }

                def allResults = new ArrayList<>()
                questions.forEach { q ->
                    List<Integer> answers = new ArrayList<>()
                    q.answers.forEach{ a -> answers.add(0)}

                    def numberCorrect = 0
                    q.responses.forEach{r ->
                        if (r.correct) {numberCorrect++}
                        r.answers.eachWithIndex { a, i ->
                            if(a) { answers.set(i, answers.get(i) + 1) }
                        }
                    }

                    def percentCorrect = q.responses != null && q.responses.size() != 0 ? numberCorrect / q.responses.size() : 0
                    allResults.add(new QuestionResult(answers: answers, correct: q.answers, percentCorrect: percentCorrect))
                }
                result.data = allResults
                result.success = true
            } else {
                result.message = "could not find course"
                result.errorCode = 400
            }
        } else {
            result.message = "students cannot get question results!"
            result.errorCode = 400
        }
        result
    }

    private boolean isSameDay(Date d1, Date d2) {
        d1.clearTime() == d2.clearTime()
    }

    /**
     * makes a Date for the current date
     * @return - returns a usable date
     */
    private Date makeDate() {
        Calendar calendar = Calendar.getInstance()
        calendar.setTime(new Date())
        return removeTime(calendar)
    }

    /**
     * makes a Date of a specific time
     * @param input - a date in the mm/dd/yyyy format
     * @return - returns a usable date
     */
    private Date makeDate(String input) {
        Calendar calendar = Calendar.getInstance()
        calendar.setTime(new Date(input))
        return removeTime(calendar)
    }

    /**
     * removes all the bs junk time stuff in a date that messes up date queries
     * @param calendar - a calender object
     * @return - returns a usable Date object
     */
    private static Date removeTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.getTime()
    }
}
