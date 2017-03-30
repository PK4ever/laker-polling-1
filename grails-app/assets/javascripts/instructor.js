var courseDeleteButtonFormatter
(function() {
    var currentInstructor

    function CurrentInstructor(token) {
        if(!token) throw Error("Token Required for Instructor")
        var _token = token
        var _courses = []

        this.setCourses = function(allCourses) {
            _courses = allCourses || []
            _courses.forEach((course) => {
                course.name = {
                    name: course.name,
                    courseId: course.id
                }
            })
            $('#courseTable').bootstrapTable({
                data: currentInstructor.getCourses()
            });
        }
        this.addCourse = function(newCourse) {
            _courses.push(newCourse)
        }

        this.getCourses = function() {
            return _courses
        }

        this.getTokenOrFetch = function(onSuccess, onFail) {
            if (_token) return onSuccess(_token)
            $.ajax({
                url: '/user/auth',
                method: 'GET',
                success: function(data) {
                    _token = data.data.token;
                    onSuccess(_token)
                },
                error: function(err) {
                    onFail(err)
                }
            });
        }

        this.getCourseById = function(courseId) {
            for (var i = 0; i < _courses.length; i++) {
                if (_courses[i].id == courseId) return _courses[i]
            }
        }
    }

    $(function() {
        $.ajax({
            url: '/user/auth',
            method: "GET",
            success: function(data){
                var token = data.data.token
                currentInstructor = new CurrentInstructor(token)
                $.ajax({
                    url: '/api/course',
                    method: "GET",
                    data: {
                        access_token: token
                    },
                    success: function(data) {
                        currentInstructor.setCourses(data.data.courses)
                    },
                    error: function() {
                        currentInstructor.setCourses(JSON.parse('[{"id":3,"name":"TCR 101","crn":"22223","students":3},{"id":4,"name":"TCR 202","crn":"22223","students":3},{"id":5,"name":"TCR 303","crn":"22223","students":3},{"id":6,"name":"TCR 404","crn":"22223","students":3}]'))
                    }
                });
            }
        });
    });


    $('#courseButton').on('click', function() {
        console.log('Clicked');

        $.ajax({
            url: '/user/auth',
            method: 'GET',
            success: function(data) {
                token = data.data.token;

                var courseName = $('#courseName').val();
                var courseCRN = $('#courseCRN').val();
                var urlstring = '/api/course?access_token=' + token + '&name=' + courseName + '&crn=' + courseCRN;
                console.log(courseName);
                console.log(courseCRN);
                console.log(urlstring);

                $.ajax({
                    url: '/api/course',
                    method: 'POST',

                    data: {
                        access_token: token,
                        name: courseName,
                        crn: courseCRN
                    },
                    success: function() {
                        document.location.href = "/dashboard";
                    }
                })
            }

        });
    });

    $('.js-deleteCourse').on('click', function() {
        var clickedButton = $(this)
        currentInstructor.getTokenOrFetch((token) => {
            var courseId = clickedButton.data("course-id")
            debugger
            var urlstring = '/api/course?access_token=' + token + '&course_id=' + courseId;
            console.log(courseId);
            console.log(courseCRN);
            console.log(urlstring);

            $.ajax({
                url: '/api/course',
                method: 'DELETE',

                data: {},
                success: function() {
                    alert("DELETED COURSE: " + courseId)
                }
            })
        }, (err) => {
            alert("SOMETHING WENT WRONG WITH DELETEING COURSE: " + err && err.message || "UNKNOWN ERROR")
        })
    });

    var preparedDeleteButton = false
    function prepareDeleteButton() {
        if(preparedDeleteButton) return
        $('.js-deleteCourseButton').click(function () {
            var course = currentInstructor.getCourseById($(this).data('course-id'))
            $('#deleteCourseModal').find('#confirmDeleteButton').data("course-id",course.id)        
            $('#deleteCourseModal').data("course-id",  course.id);
            $('#deleteCourseModal').find('#courseId').html(course.id)
            $('#deleteCourseModal').find('#courseName').html(course.name)
        })
        preparedDeleteButton = true
    }
    
    courseDeleteButtonFormatter = function(value, row, index) {
        var course = currentInstructor.getCourseById(value)
        var deleteButton = '<button class="btn btn-danger js-deleteCourseButton" type="button" data-toggle="modal" data-target="#deleteCourseModal" data-course-id="' + value + '">'
        deleteButton += 'Delete'
        deleteButton += '</button>'
        setTimeout(() => {
            prepareDeleteButton()
        }, 500)
        return deleteButton
    }
})()

function identifierFormatter(value, row, index) {
    return [
        '<a href="/course/' + value.courseId + '" data-course-id="' + value.courseId + '">',
        value.name,
        '</a>'].join('');
}