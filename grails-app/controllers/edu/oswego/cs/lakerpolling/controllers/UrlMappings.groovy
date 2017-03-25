package edu.oswego.cs.lakerpolling.controllers

class UrlMappings {

    static mappings = {

        /* Page url mapping */
        "/"(controller: 'application', action: 'landing')
        "/dashboard"(controller: 'application', action: 'dashboard')

        /* end Page url mapping */

        "/user/auth"(controller: 'auth', action: 'auth', method:'post')
        "/user/logout"(controller: 'auth', action: 'logout', method: 'post')

        /* API endpoints mapping */

//        group "/api/user", {
//            "/auth"(controller: 'user', action: 'auth')
//        }

        group "/api/course", {
            "/"(controller: 'course', action: 'courseGet', method : 'get')
            "/"(controller: 'course', action: 'postCourse', method : 'post')
            "/"(controller: 'course', action: 'deleteCourse', method : 'delete')

            "/student"(controller: 'course', action: 'getCourseStudent', method: 'get')
            "/student"(controller: 'course', action: 'postCourseStudent', method: 'post')
            "/student"(controller: 'course', action: 'deleteCourseStudent', method: 'delete')
        }
    }
}
