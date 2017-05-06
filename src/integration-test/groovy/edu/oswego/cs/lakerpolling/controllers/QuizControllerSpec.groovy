package edu.oswego.cs.lakerpolling.controllers

import edu.oswego.cs.lakerpolling.BootStrapSpec
import edu.oswego.cs.lakerpolling.domains.Course
import edu.oswego.cs.lakerpolling.domains.Quiz
import edu.oswego.cs.lakerpolling.domains.User

import java.text.SimpleDateFormat

class QuizControllerSpec extends BootStrapSpec {

    void "Test postQuiz(): 1 - Valid Instructor"() {
        given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, VALID_COURSE, VALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("name",VALID_QUIZ.name)
        params.put("start_timestamp", VALID_QUIZ.startDate.time)
        params.put("end_timestamp", VALID_QUIZ.endDate.time)
        params.put("course_id", VALID_COURSE.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "PostQuiz Is Queried"
        def response = post("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
    }

    void "Test postQuiz(): 2 - Valid Admin"() {
        given: "The Following Parameters"
        testWith(VALID_ADMIN, VALID_COURSE, VALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("name",VALID_QUIZ.name)
        params.put("start_timestamp", VALID_QUIZ.startDate.time)
        params.put("end_timestamp", VALID_QUIZ.endDate.time)
        params.put("course_id", VALID_COURSE.id)
        params.put("access_token", VALID_ADMIN.authToken.accessToken)

        when: "PostQuiz Is Queried"
        def response = post("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
    }

    void "Test postQuiz(): 3 - Valid Student"() {
        given: "The Following Parameters"
        testWith(VALID_STUDENT, VALID_COURSE, VALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("name",VALID_QUIZ.name)
        params.put("start_timestamp", VALID_QUIZ.startDate.time)
        params.put("end_timestamp", VALID_QUIZ.endDate.time)
        params.put("course_id", VALID_COURSE.id)
        params.put("access_token", VALID_STUDENT.authToken.accessToken)

        when: "PostQuiz Is Queried"
        def response = post("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 403
        response.json.status == "failure"
    }

    void "Test postQuiz(): 4 - Invalid Instructor"() {
        given: "The Following Parameters"
        testWith(INVALID_INSTRUCTOR, VALID_COURSE, VALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("name",VALID_QUIZ.name)
        params.put("start_timestamp", VALID_QUIZ.startDate.time)
        params.put("end_timestamp", VALID_QUIZ.endDate.time)
        params.put("course_id", VALID_COURSE.id)
        params.put("access_token", INVALID_INSTRUCTOR.authToken.accessToken)

        when: "PostQuiz Is Queried"
        def response = post("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 401
        response.json.status == "failure"
    }

    void "Test postQuiz(): 5 - Invalid Course"() {
        given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, INVALID_COURSE, VALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("name",VALID_QUIZ.name)
        params.put("start_timestamp", VALID_QUIZ.startDate.time)
        params.put("end_timestamp", VALID_QUIZ.endDate.time)
        params.put("course_id", INVALID_COURSE.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "PostQuiz Is Queried"
        def response = post("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 400
        response.json.status == "failure"
    }

    void "Test postQuiz(): 6 - Invalid Start End Time"() {
        given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, VALID_COURSE, INVALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("name",INVALID_QUIZ.name)
        params.put("start_timestamp", INVALID_QUIZ.startDate.time)
        params.put("end_timestamp", INVALID_QUIZ.endDate.time)
        params.put("course_id", VALID_COURSE.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "PostQuiz Is Queried"
        def response = post("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 400
        response.json.status == "failure"
    }

    void "Test getQuizzes(): 1 - Valid Instructor"() {
        given: "The Following Parameters"
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        format.setTimeZone(TimeZone.getTimeZone("UTC"))

        testWith(VALID_INSTRUCTOR, VALID_QUIZ, VALID_QUIZ.course, VALID_QUIZ2)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id",  VALID_QUIZ.course.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = get("/api/quiz", params)
        def quiz = response.json.data.quizzes[0]

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
        2.times {
            def validQuiz
            if(quiz.name == VALID_QUIZ.name) {
                validQuiz = VALID_QUIZ
            } else {
                validQuiz = VALID_QUIZ2
            }

            assert quiz.name == validQuiz.name
            assert quiz.id == validQuiz.id
            assert quiz.startDate == format.format(validQuiz.startDate)
            assert quiz.endDate == format.format(validQuiz.endDate)

            quiz = response.json.data.quizzes[1]
        }
    }

    void "Test getQuizzes(): 2 - Valid Admin"() {
        given: "The Following Parameters"
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        format.setTimeZone(TimeZone.getTimeZone("UTC"))

        testWith(VALID_ADMIN, VALID_QUIZ, VALID_QUIZ.course, VALID_QUIZ2)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id",  VALID_QUIZ.course.id)
        params.put("access_token", VALID_ADMIN.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = get("/api/quiz", params)
        def quiz = response.json.data.quizzes[0]

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
        2.times {
            def validQuiz
            if(quiz.name == VALID_QUIZ.name) {
                validQuiz = VALID_QUIZ
            } else {
                validQuiz = VALID_QUIZ2
            }

            assert quiz.name == validQuiz.name
            assert quiz.id == validQuiz.id
            assert quiz.startDate == format.format(validQuiz.startDate)
            assert quiz.endDate == format.format(validQuiz.endDate)

            quiz = response.json.data.quizzes[1]
        }
    }

    void "Test getQuizzes(): 3 - Valid Student"() {
        given: "The Following Parameters"
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        format.setTimeZone(TimeZone.getTimeZone("UTC"))

        VALID_COURSE.addToStudents(VALID_STUDENT)
        testWith(VALID_STUDENT, VALID_QUIZ, VALID_QUIZ.course, VALID_QUIZ2)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id",  VALID_QUIZ.course.id)
        params.put("access_token", VALID_STUDENT.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = get("/api/quiz", params)
        def quiz = response.json.data.quizzes[0]

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
        2.times {
            def validQuiz
            if(quiz.name == VALID_QUIZ.name) {
                validQuiz = VALID_QUIZ
            } else {
                validQuiz = VALID_QUIZ2
            }

            assert quiz.name == validQuiz.name
            assert quiz.id == validQuiz.id
            assert quiz.startDate == format.format(validQuiz.startDate)
            assert quiz.endDate == format.format(validQuiz.endDate)

            quiz = response.json.data.quizzes[1]
        }
    }

    void "Test getQuizzes(): 4 - Valid Student Not In Course"() {
        given: "The Following Parameters"
        testWith(VALID_STUDENT, VALID_QUIZ, VALID_QUIZ.course, VALID_QUIZ2)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id",  VALID_QUIZ.course.id)
        params.put("access_token", VALID_STUDENT.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = get("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 401
        response.json.status == "failure"
    }

    void "Test getQuizzes(): 5 - Valid Instructor"() {
        given: "The Following Parameters"
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        format.setTimeZone(TimeZone.getTimeZone("UTC"))

        testWith(VALID_INSTRUCTOR, VALID_QUIZ, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id",  VALID_QUIZ.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = get("/api/quiz", params)
        def quiz = response.json.data.quiz

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
        quiz.name == VALID_QUIZ.name
        quiz.id == VALID_QUIZ.id
        quiz.startDate == format.format(VALID_QUIZ.startDate)
        quiz.endDate == format.format(VALID_QUIZ.endDate)
    }

    void "Test getQuizzes(): 6 - Valid Admin"() {
        given: "The Following Parameters"
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        format.setTimeZone(TimeZone.getTimeZone("UTC"))

        testWith(VALID_ADMIN, VALID_QUIZ, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id",  VALID_QUIZ.id)
        params.put("access_token", VALID_ADMIN.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = get("/api/quiz", params)
        println response.json
        def quiz = response.json.data.quiz

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
        quiz.name == VALID_QUIZ.name
        quiz.id == VALID_QUIZ.id
        quiz.startDate == format.format(VALID_QUIZ.startDate)
        quiz.endDate == format.format(VALID_QUIZ.endDate)
    }

    void "Test getQuizzes(): 7 - Valid Student"() {
        given: "The Following Parameters"
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        format.setTimeZone(TimeZone.getTimeZone("UTC"))

        VALID_COURSE.addToStudents(VALID_STUDENT)
        testWith(VALID_STUDENT, VALID_QUIZ, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id",  VALID_QUIZ.id)
        params.put("access_token", VALID_STUDENT.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = get("/api/quiz", params)
        def quiz = response.json.data.quiz

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
        quiz.name == VALID_QUIZ.name
        quiz.id == VALID_QUIZ.id
        quiz.startDate == format.format(VALID_QUIZ.startDate)
        quiz.endDate == format.format(VALID_QUIZ.endDate)
    }

    void "Test getQuizzes(): 8 - Valid Student Not In Course"() {
        given: "The Following Parameters"
        testWith(VALID_STUDENT, VALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id",  VALID_QUIZ.id)
        params.put("access_token", VALID_STUDENT.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = get("/api/quiz", params)
        def quiz = response.json.data.quiz

        then: "The Output Should Be The Following"
        response.status == 403
        response.json.status == "failure"
    }

    void "Test getQuizzes(): 9 - Invalid Course"() {
        given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, INVALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id",  INVALID_COURSE.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = get("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 400
        response.json.status == "failure"
    }

    void "Test getQuizzes(): 10 - Invalid Quiz"() {
        given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, INVALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id",  INVALID_QUIZ.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = get("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 400
        response.json.status == "failure"
    }

    void "Test getQuizzes(): 11 - Invalid Instructor"() {
        given: "The Following Parameters"
        testWith(INVALID_INSTRUCTOR, VALID_COURSE, VALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id", VALID_QUIZ.id)
        params.put("course_id",  VALID_COURSE.id)
        params.put("access_token", INVALID_INSTRUCTOR.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = get("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 401
        response.json.status == "failure"
    }

    void "Test getQuizzes(): 12 - Valid Instructor Not In Course"() {
        given: "The Following Parameters"
        User.withTransaction {
            INVALID_INSTRUCTOR.id = null
            INVALID_INSTRUCTOR.save(flush: true, failOnError: true)
            VALID_COURSE.setInstructor(INVALID_INSTRUCTOR)
        }
        testWith(VALID_INSTRUCTOR, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id",  VALID_COURSE.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = get("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 403
        response.json.status == "failure"
    }

    void "Test deleteQuiz(): 1 - Valid Instructor"() {
        given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, VALID_QUIZ, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id",  VALID_QUIZ.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = delete("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
        Quiz.withTransaction { Quiz.get(VALID_QUIZ.id) } == null
    }

    void "Test deleteQuiz(): 2 - Valid Admin"() {
        given: "The Following Parameters"
        testWith(VALID_ADMIN, VALID_QUIZ, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id",  VALID_QUIZ.id)
        params.put("access_token", VALID_ADMIN.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = delete("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
        Quiz.withTransaction { Quiz.get(VALID_QUIZ.id) } == null
    }

    void "Test deleteQuiz(): 3 - Valid Student"() {
        given: "The Following Parameters"
        testWith(VALID_STUDENT, VALID_QUIZ, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id",  VALID_QUIZ.id)
        params.put("access_token", VALID_STUDENT.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = delete("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 401
        response.json.status == "failure"
    }

    void "Test deleteQuiz(): 4 - Invalid Instructor"() {
        given: "The Following Parameters"
        testWith(INVALID_INSTRUCTOR, VALID_QUIZ, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id",  VALID_QUIZ.id)
        params.put("access_token", INVALID_INSTRUCTOR.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = delete("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 401
        response.json.status == "failure"
    }

    void "Test deleteQuiz(): 5 - Valid Instructor Not In Course"() {
        given: "The Following Parameters"
        User.withTransaction {
            INVALID_INSTRUCTOR.id = null
            INVALID_INSTRUCTOR.save(flush: true, failOnError: true)
            VALID_COURSE.setInstructor(INVALID_INSTRUCTOR)
        }
        testWith(VALID_INSTRUCTOR, VALID_QUIZ, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id",  VALID_QUIZ.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = delete("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 401
        response.json.status == "failure"
    }

    void "Test deleteQuiz(): 3 - Invalid Quiz"() {
        given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, INVALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id",  INVALID_QUIZ.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "GetQuiz Is Queried"
        def response = delete("/api/quiz", params)

        then: "The Output Should Be The Following"
        response.status == 400
        response.json.status == "failure"
    }

    void "Test submitQuiz(): 1 - Valid Instructor"() {
        given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, VALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id", VALID_QUIZ.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "SubmitQuiz Queried"
        def response = post("/api/quiz/submission", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
    }

    void "Test submitQuiz(): 3 - Valid Student"() {
        given: "The Following Parameters"
        Course.withTransaction {
            VALID_COURSE.addToStudents(VALID_STUDENT)
        }
        testWith(VALID_STUDENT, VALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id", VALID_QUIZ.id)
        params.put("access_token", VALID_STUDENT.authToken.accessToken)

        when: "SubmitQuiz Queried"
        def response = post("/api/quiz/submission", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
    }

    void "Test submitQuiz(): 4 - Invalid Instructor"() {
        given: "The Following Parameters"
        testWith(INVALID_INSTRUCTOR, VALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id", VALID_QUIZ.id)
        params.put("access_token", INVALID_INSTRUCTOR.authToken.accessToken)

        when: "SubmitQuiz Queried"
        def response = post("/api/quiz/submission", params)

        then: "The Output Should Be The Following"
        response.status == 401
        response.json.status == "failure"
    }

    void "Test submitQuiz(): 5 - Valid Instructor with Invalid Quiz"() {
        given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, INVALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id", INVALID_QUIZ.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "SubmitQuiz Queried"
        def response = post("/api/quiz/submission", params)

        then: "The Output Should Be The Following"
        response.status == 400
        response.json.status == "failure"
    }

    void "Test getQuizSubmission(): 1 - Valid Instructor"() {
        given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, VALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id", VALID_QUIZ.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "GetQuizSubmission Queried"
        def response = get("/api/quiz/submission", params)

        then: "The Output Should Be The Following"
        response.status == 400
        response.json.status == "success"
    }

    void "Test getQuizSubmission(): 3 - Valid Student"() {
        given: "The Following Parameters"
        Course.withTransaction {
            VALID_COURSE.addToStudents(VALID_STUDENT)
        }
        testWith(VALID_STUDENT, VALID_QUIZ)

        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id", VALID_QUIZ.id)
        params.put("access_token", VALID_STUDENT.authToken.accessToken)

        when: "GetQuizSubmission Queried"
        post("/api/quiz/submission", params) // Submit a quiz before checking for quiz submissions
        def response = get("/api/quiz/submission", params)

        then: "The Output Should Be The Following"
        response.status == 200
        response.json.status == "success"
    }

    void "Test getQuizSubmission(): 4 - Invalid Instructor"() {
        given: "The Following Parameters"
        testWith(INVALID_INSTRUCTOR, VALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id", VALID_QUIZ.id)
        params.put("access_token", INVALID_INSTRUCTOR.authToken.accessToken)

        when: "GetQuizSubmission Queried"
        def response = get("/api/quiz/submission", params)

        then: "The Output Should Be The Following"
        response.status == 401
        response.json.status == "failure"
    }

    void "Test getQuizSubmission(): 5 - Valid Instructor with Invalid Quiz"() {
        given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, INVALID_QUIZ)
        Map<String, Object> params = new HashMap<>()
        params.put("quiz_id", INVALID_QUIZ.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)

        when: "GetQuizSubmission Queried"
        def response = get("/api/quiz/submission", params)

        then: "The Output Should Be The Following"
        response.status == 400
        response.json.status == "failure"
    }

    void "Test downloadGrades(): 1 - Valid Instructor"() {
        given: "The Following Parameters"
        testWith(VALID_INSTRUCTOR, VALID_COURSE)
        Map<String, Object> params = new HashMap<>()
        params.put("course_id", VALID_COURSE.id)
        params.put("access_token", VALID_INSTRUCTOR.authToken.accessToken)
    }
}
