package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.domains.*
import edu.oswego.cs.lakerpolling.util.RoleType
import grails.transaction.Transactional

@Transactional
class QuestionService {

    def createQuestion(AuthToken token, String question, String course_id, String answers, String date) {
        def user = token.user
        List<String> tempList = answers.indexOf(",") != -1 ? answers.split(",").toList(): [answers]
        List<Boolean> answerList = new ArrayList<>()

        tempList.forEach {s -> answerList.add(s.asBoolean())}
        if (user) {
            if (user.role.type == RoleType.INSTRUCTOR) {
                Course course = Course.findById(course_id.toLong())
                if (course) {
                    Question newQuestion
                    if (question != null) {
                        newQuestion = new Question(course: course, answers: answerList, question: question)
                    } else {
                        newQuestion = new Question(course: course, answers: answerList)
                    }
                    newQuestion.active = false
                    newQuestion.studentAnswers = new ArrayList<>()
                    answerList.each { i -> newQuestion.studentAnswers.add(0) }
                    newQuestion.save(flush: true, failOnError: true)
                    def attendance = Attendance.findByDateAndCourse(makeDate(date), course)
                    if (attendance == null) attendance = new Attendance(date: makeDate(date), course: course)
                    course.students.each { s -> new Attendee(attended: false, attendance: attendance, student: s).save(flush: true, failOnError: true) }
                    newQuestion
                } else null
            } else null
        } else null
    }

    def answerQuestion(AuthToken token, String question_id, String answer, String date) {
        def user = token.user
        if(user) {
            if(user.role.getType() == RoleType.STUDENT) {
                List<String> tempList = answer.indexOf(",") != -1 ? answer.split(",").toList(): [answer]
                List<Boolean> answerList = new ArrayList<>()
                tempList.forEach {s -> answerList.add(s.asBoolean())}
                def question = Question.findById(question_id.toLong())
                if(question) {
                    def attendee = Attendance.findByDateAndCourse(makeDate(date), question.course).attendees.find { a -> a.student == user }
                    if(attendee) {
                        def isRight = false
                        def realAnswers = question.answers
                        realAnswers.eachWithIndex { a, i ->
                            isRight = (a == answerList.get(i))
                            if (answerList.get(i)) question.studentAnswers.add(i, question.studentAnswers.get(i) + 1)
                        }
                        attendee.attended = true
                        new Answer(correct: isRight, question: question, student: user).save(flush: true, failOnError: true)
                        attendee.attended
                    } else false
                } else false
            } else false
        } else false
    }

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

    private Date makeDate() {
        Calendar calendar = Calendar.getInstance()
        calendar.setTime(new Date())
        return removeTime(calendar)
    }

    private Date makeDate(String input) {
        Calendar calendar = Calendar.getInstance()
        calendar.setTime(new Date(input))
        return removeTime(calendar)
    }

    private static Date removeTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.getTime()
    }
}
