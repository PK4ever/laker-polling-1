package edu.oswego.cs.lakerpolling.controllers

class UrlMappings {

    static mappings = {

        /* Page url mapping */
        "/"(controller: 'application', action: 'landing')
        "/dashboard"(controller: 'application', action: 'dashboard')
        "/course"(controller: 'application', action: 'courseView')
        "/course/roster"(controller: 'application', action: 'classRoster')
        "/course/attendance"(controller: 'application', action: 'classAttendance')

        /* end Page url mapping */

        /* Auth endpoints */
        "/user/auth"(controller: 'auth', action: 'auth', method:'post')
        "/user/auth"(controller: 'auth', action: 'current', method:'get')
        "/user/logout"(controller: 'auth', action: 'logout', method: 'post')

        /* API endpoints mapping */
        group "/api/course", {
            "/"(controller: 'course', action: 'courseGet', method : 'get')
            "/"(controller: 'course', action: 'postCourse', method : 'post')
            "/"(controller: 'course', action: 'deleteCourse', method : 'delete')

            "/student"(controller: 'course', action: 'getCourseStudent', method: 'get')
            "/student"(controller: 'course', action: 'postCourseStudent', method: 'post')
            "/student"(controller: 'course', action: 'deleteCourseStudent', method: 'delete')

            "/attendance"(controller: 'course', action: 'getAttendance', method: 'get')
        }

        group "/api/question", {
            "/"(controller: 'question', action: 'createQuestion', method: 'post')
            "/"(controller: 'question', action: 'changeQuestionStatus', method: 'put')
            "/"(controller: 'question', action: 'getQuestion', method: 'get')
            "/active"(controller: 'question', action: 'getActiveQuestion', method: 'get')

            "/answer"(controller: 'question', action: 'getAnswers', mehtod: 'get')
            "/answer"(controller: 'question', action: 'answerQuestion', method: 'put')
        }
    }
}
