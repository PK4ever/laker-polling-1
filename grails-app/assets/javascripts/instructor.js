var courseDeleteButtonFormatter
var identifierFormatter
var currentInstructor
var courseId
(function() {
    function InstructorNetworkService(instructor) {
        var _instructor = instructor
        this.deleteCourseById = function(courseId, onSuccess, onFail) {
            _instructor.getTokenOrFetch((token) => {
                var urlstring = '/api/course?access_token=' + token + '&course_id=' + courseId;
                $.ajax({
                    url: urlstring,
                    method: 'DELETE',
                    success: function() {
                        onSuccess(courseId)
                    },
                    error: function(err) {
                        onFail(err)
                    }
                })
            }, onFail)
        }

        this.getToken = function(onSuccess, onFail) {
            $.ajax({
                url: '/user/auth',
                method: 'GET',
                success: function(data) {
                    onSuccess(data.data.token)
                },
                error: function(err) {
                    onFail(err)
                }
            });
        }
    }
    function CurrentInstructor(token) {
        if(!token) throw Error("Token Required for Instructor")
        var _token = token
        var _courses = []
        var _service = new InstructorNetworkService(this)

        this.setCourses = function(allCourses) {
            _courses = allCourses || []
            this.refreshCourseTable()
        }
        this.addCourse = function(newCourse) {
            _courses.push(newCourse)
        }

        this.getCourses = function() {
            return _courses
        }

        this.getTokenOrFetch = function(onSuccess, onFail) {
            if (_token) return onSuccess(_token)
            _service.getToken((token) => {
                _token = token;
                onSuccess(token)
            }, onFail)
        }

        this.getCourseById = function(courseId) {
            for (var i = 0; i < _courses.length; i++) {
                if (_courses[i].id == courseId) return _courses[i]
            }
        }

        this.removeCourseById = function(courseId) {
            for (var i = 0; i < _courses.length; i++) {
                if (_courses[i].id == courseId) {
                    var course = _courses[i]
                    delete _courses[courseId]
                    this.refreshCourseTable()
                    return course
                }
            }
        }

        this.deleteCourseById = function(courseId, onSuccess, onFail) {
            _service.deleteCourseById(courseId, (courseId) => {
                onSuccess(this.removeCourseById(courseId))
            }, onFail)
        }

        this.refreshCourseTable = function() {
            $('#courseTable').bootstrapTable({
                data: currentInstructor.getCourses()
            });
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
                        var course = currentInstructor.getCourseById(courseId)
                        $('#coursePageTitle').html(course.name)
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
        currentInstructor.deleteCourseById($(this).data("course-id"), (course) => {
            //alert("DELETED COURSE BY ID: " + JSON.stringify(course))
            window.location.reload()
        }, (err) => {
            alert(JSON.stringify(err))
        })
    });

    var preparedDeleteButton = false
    function prepareDeleteButton() {
        if(preparedDeleteButton) return
        $('.js-deleteCourseButton').click(function () {
            const clickedButton = $(this)
            debugger
            const courseId = clickedButton.data('course-id')
            var course = currentInstructor.getCourseById(courseId)
            if (!course) {
                alert("Course not found by id " + courseId)
            }
            $('#deleteCourseModal').find('#confirmDeleteButton').data("course-id",course.id)
            $('#deleteCourseModal').data("course-id",  course.id);
            $('#deleteCourseModal').find('#courseId').html(course.id)
            $('#deleteCourseModal').find('#courseName').html(course.name)
        })
        preparedDeleteButton = true
    }
    courseDeleteButtonFormatter = function(_, course, index) {
        var course = currentInstructor.getCourseById(course.id)
        if(!course) return '<span style="color:red">Invalid Course</span>'
        var deleteButton = '<button class="btn btn-danger js-deleteCourseButton" type="button" data-toggle="modal" data-target="#deleteCourseModal" data-course-id="' + course.id + '">'
        deleteButton += 'Delete'
        deleteButton += '</button>'
        setTimeout(() => {
            prepareDeleteButton()
        }, 500)
        return deleteButton
    }

    function prepareClassTitle(courseId) {
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
                        var course = currentInstructor.getCourseById(courseId)
                        console.log(course)
                        $('#coursePageTitle').html(course.name)
                    },
                    error: function() {
                        currentInstructor.setCourses(JSON.parse('[{"id":3,"name":"TCR 101","crn":"22223","students":3},{"id":4,"name":"TCR 202","crn":"22223","students":3},{"id":5,"name":"TCR 303","crn":"22223","students":3},{"id":6,"name":"TCR 404","crn":"22223","students":3}]'))
                    }
                });
            }
        });

    }
    identifierFormatter = function(_, course, index) {
        return [
            '<a href="/course?courseId='+ course.id +'" class="btn btn-link" onClick="prepareClassTitle('+ course.id +')">',
            course.name,
            '</a>'].join('');
    }

    $(document).ready(function() {
        $.ajax({
            url: '/user/auth',
            method: "GET",
            success: function(data){
                var token = data.data.token
                currentInstructor = new CurrentInstructor(token)
            }
        });
    })
})()

function prepareClassTitle(cId) {
    courseId = cId;
}