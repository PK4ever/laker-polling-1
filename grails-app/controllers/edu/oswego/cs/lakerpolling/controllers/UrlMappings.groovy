package edu.oswego.cs.lakerpolling.controllers

class UrlMappings {

    static mappings = {

        group "/api/user", {
            "/auth"(controller: 'user', action: 'auth')
        }

        group "/api/course", {
            "/"(controller: 'course', action: 'courseGet', method : 'get')
            "/"(controller: 'course', action: 'postCourse', method : 'post')
            "/"(controller: 'course', action: 'deleteCourse', method : 'delete')

            "/student"(controller: 'course', action: 'getCourseStudent', method: 'get')
            "/student"(controller: 'course', action: 'postCourseStudent', method: 'post')
            "/student"(controller: 'course', action: 'deleteCourseStudent', method: 'delete')
        }

        "/**"(controller: 'application', action: 'index')
    }
}
