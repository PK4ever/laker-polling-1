var courseDeleteButtonFormatter
var identifierFormatter
var studentDeleteButtonFormatter
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

        this.deleteStudentById = function(studentId, onSuccess, onFail){
            _instructor.getTokenOrFetch((token) => {
                //debugger
                var urlString = '/api/course/student?access_token=' + token + '&course_id=' + courseId + '&user_id=' + studentId;
                $.ajax({
                    url: urlString,
                    method: 'DELETE',
                    success: function(){
                        onSuccess(studentId)
                    },
                    error: function(err){
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
        if(!token) throw Error("Token Required for Instructor");
        var _token = token;
        var _courses = [];
        var _service = new InstructorNetworkService(this);
        var _roster;

        this.setCourses = function(allCourses) {
            _courses = allCourses || []
            this.refreshCourseTable()
        };
        this.addCourse = function(newCourse) {
            _courses.push(newCourse)
        };

        this.getCourses = function() {
            return _courses
        };

        this.setRoster = function(course_id) {
              this.refreshStudentTable();
        };

        this.getTokenOrFetch = function(onSuccess, onFail) {
            if (_token) return onSuccess(_token)
            _service.getToken((token) => {
                _token = token;
            onSuccess(token)
            }, onFail)
        };

        this.getCourseById = function(courseId) {
            for (var i = 0; i < _courses.length; i++) {
                if (_courses[i].id == courseId) return _courses[i]
            }
        }

        this.getStudentById = function (studentId) {
            for (var i = 0; i < _roster.length; i++){
                if (_roster[i].id == studentId) return _roster[i]
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

        this.removeStudentById = function (studentId){
            for (var i = 0; i < _roster.length; ++i) {
                if(_roster[i].id == studentId){
                    var student = _roster[i]
                    delete _roster[i]
                    this.refreshStudentTable()
                    return student
                }
            }
        }

        this.deleteStudentById = function(studentId, onSuccess, onFail){
            //debugger
            _service.deleteStudentById(studentId, (studentId) => {
                onSuccess(this.removeStudentById(studentId))
            }, onFail)

        }

        this.deleteCourseById = function(courseId, onSuccess, onFail) {
            _service.deleteCourseById(courseId, (courseId) => {
                onSuccess(this.removeCourseById(courseId))
            }, onFail)
        };

        this.refreshCourseTable = function() {
            $('#courseTable').bootstrapTable({
                data: currentInstructor.getCourses()
            });
        };
        this.refreshStudentTable = function(){
            _roster = getCourseRoster(courseId);
            $('#studentTable').bootstrapTable({
                data: _roster
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
                        currentInstructor.setCourses(data.data.courses);
                        if (courseId) {
                            var course = currentInstructor.getCourseById(courseId);
                            currentInstructor.setRoster(courseId);
                            $('#coursePageTitle').html(course.name)
                        }
                    },
                    error: function() {
                        currentInstructor.setCourses(JSON.parse('[{"id":3,"name":"TCR 101","crn":"22223","students":3},{"id":4,"name":"TCR 202","crn":"22223","students":3},{"id":5,"name":"TCR 303","crn":"22223","students":3},{"id":6,"name":"TCR 404","crn":"22223","students":3}]'))
                    }
                });
            }
        });
    });

    function getCourseRoster(course_id) {
        var result = [1,2,3,4];
        $.ajax({
            url: '/user/auth',
            type: 'GET',
            async: false,
            success: function(data) {
                var token = data.data.token;
                $.ajax({
                    url: '/api/course/student?access_token=' + token + '&course_id=' + course_id,
                    type: 'GET',
                    async: false,
                    success: function(stuff) {
                        result = stuff.data.students;

                    },
                    error: function(err) {
                        // console.log(err);
                    }
                });
            }
        });
        return result;
    }


    $('#courseButton').on('click', function() {
        $.ajax({
            url: '/user/auth',
            method: 'GET',
            success: function(data) {
                token = data.data.token;
                console.log(token); // temp

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

    $('#csv-form').submit(function(event) {
        event.preventDefault();
        $.ajax({
            url: '/user/auth',
            type: 'GET',
            success: function(data) {
                var token = data.data.token;
                console.log(token);
                var formData = new FormData();
                formData.append('file', $('#csv-file')[0].files[0]);

                $.ajax({
                    url: '/api/course/student?access_token=' + token + '&course_id=' + courseId,
                    type: 'POST',
                    data: formData,
                    cache: false,
                    async: false,
                    contentType: false,
                    processData: false,
                    success: function(data) {
                        console.log(formData.get('file').length);
                        window.location.reload();
                    },
                    error: function(jqXHR, textStatus, errorMessage) {
                        console.log(textStatus)
                    }

                });
            }
        });
    });

    $('#csv-form-email').submit(function(event) {
        $.ajax({
            url: '/user/auth',
            type: 'GET',
            success: function(data) {
                var token = data.data.token;
                var email = $("#email").val();

                console.log(email);

                $.ajax({
                    url: '/api/course/student?access_token=' + token + '&course_id=' + courseId + '&email=' + email,
                    type: 'POST',
                    success: function(data) {
                        console.log('Works');
                        // window.location.reload();
                    },
                    error: function(jqXHR, textStatus, errorMessage) {
                        console.log(errorMessage)
                    }

                });
            }
        });
    });

    $('.js-deleteStudent').click(function () {
        currentInstructor.deleteStudentById($(this).data("student-id"), (student) => {
            alert("REMOVED STUDENT: " + JSON.stringify(student))
            window.location.reload()
        },
        (err) => {
            alert(JSON.stringify(err))
        })
    });


    $('.js-deleteCourse').on('click', function() {
        currentInstructor.deleteCourseById($(this).data("course-id"), (course) => {
            //alert("DELETED COURSE BY ID: " + JSON.stringify(course))
            window.location.reload()
    }, (err) => {
            alert(JSON.stringify(err))
        })
    });

    var preparedDeleteButton = false;
    function prepareDeleteButton() {
        if(preparedDeleteButton) return;
        $('.js-deleteCourseButton').click(function () {
            const clickedButton = $(this);
            // debugger
            const courseId = clickedButton.data('course-id');
            var course = currentInstructor.getCourseById(courseId);
            if (!course) {
                alert("Course not found by id " + courseId);
            }
            $('#deleteCourseModal').find('#confirmDeleteButton').data("course-id",course.id);
            $('#deleteCourseModal').data("course-id",  course.id);
            $('#deleteCourseModal').find('#courseId').html(course.id);
            $('#deleteCourseModal').find('#courseName').html(course.name)
        });
        preparedDeleteButton = true
    }

    var preparedSDelete = false
    function prepareStudentDeleteButton() {
        if(preparedSDelete) return;
        $('.js-deleteStudentButton').click(function () {
            const clickedSButton = $(this)
            const sId = clickedSButton.data('student-id');
            var student = currentInstructor.getStudentById(sId);
            $('#deleteStudentModal').find('#confirmDeleteStudent').data("student-id", student.id);
            $('#deleteStudentModal').data("student-id", student.id);
            $('#deleteStudentModal').find('#studentName').html(student.first + ' ' + student.last);
        })
        prepareSDelete = true;
    }

    courseDeleteButtonFormatter = function(_, course, index) {
        var course = currentInstructor.getCourseById(course.id);
        if(!course) return '<span style="color:red">Invalid Course</span>';
        var deleteButton = '<button class="btn btn-danger js-deleteCourseButton" type="button" data-toggle="modal" data-target="#deleteCourseModal" data-course-id="' + course.id + '">'
        deleteButton += 'Delete'
        deleteButton += '</button>'
        setTimeout(() => {
            prepareDeleteButton()
        }, 500)
        return deleteButton
    }


    studentDeleteButtonFormatter = function(_, student, index) {
        var deleteStudentButton = '<button class="btn btn-danger js-deleteStudentButton" type="button" data-toggle="modal" data-target="#deleteStudentModal" data-student-id="' + student.id + '">'
        deleteStudentButton += 'Delete'
        deleteStudentButton += '</button>'
        setTimeout(() => {
            prepareStudentDeleteButton()
        }, 500)
        return deleteStudentButton
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
                        currentInstructor.setCourses(data.data.courses);
                        var course = currentInstructor.getCourseById(courseId);
                        console.log(course);
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
    };

    $(document).ready(function() {
        $.ajax({
            url: '/user/auth',
            method: "GET",
            success: function(data){
                var token = data.data.token;
                currentInstructor = new CurrentInstructor(token)
            }
        });
    })
})();

function prepareClassTitle(cId) {
    courseId = cId;
}