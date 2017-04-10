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
        var _isInDeleteCoursesMode = false

        this.setCourses = function(allCourses) {
            _courses = allCourses || []
            if(location.pathname.substring(location.pathname.lastIndexOf("/") + 1) == "dashboard") {
                this.refreshCourseTable()
            }
        }
        this.addCourse = function(newCourse) {
            _courses.push(newCourse)
        };

        this.getCourses = function() {
            return _courses
        };

        this.setRoster = function(course_id) {
              this.refreshStudentTable();
        };

        this.setRoster = function(course_id) {
              this.refreshStudentTable();
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
                if (_courses[i].id == courseId) {
                    return _courses[i]
                }
            }
        }

        this.getStudentById = function (studentId) {
            for (var i = 0; i < _roster.length; i++){
                if (_roster[i].id == studentId) return _roster[i]
            }
        }

        this.getStudentById = function (studentId) {
            for (var i = 0; i < _roster.length; i++){
                if (_roster[i].id == studentId) return _roster[i]
            }
        }

        this.removeCourseById = function(courseId) {
            var removedCourse
            _courses = _courses.filter((course) => {
                if (course.id == courseId) removedCourse = course
                return course.id != courseId
            })
            setTimeout(() => {
                this.refreshCourseTable()
            }, 0)
            return removedCourse
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
            $("#courses").html("")
            for (var i = 0, length = _courses.length; i < length; i++) {
                var div = document.createElement("div")
                div.innerHTML = courseHTML(_courses[i], _isInDeleteCoursesMode)
                $("#courses").append(div);
            }
            if (_isInDeleteCoursesMode) prepareDeleteButton()
        };
        this.refreshStudentTable = function(){
            _roster = getCourseRoster(courseId);
            $('#studentTable').bootstrapTable({
                data: _roster
            });
        }

        this.toggleDeleteCoursesMode = function(enabled) {
            _isInDeleteCoursesMode = !!enabled
            this.refreshCourseTable()
        }

        this.isInDeleteCoursesMode = function() {
            return _isInDeleteCoursesMode
        }
    }

    $(function() {
        $.ajax({
            url: '/user/auth',
            method: "GET",
            success: function(data){
                var token = data.data.token
                console.log(token)
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
                            if(location.pathname.substring(location.pathname.lastIndexOf("/") + 1) == "roster") {
                            currentInstructor.setRoster(courseId);
                            }
                            $('#coursePageTitle').html(course.name);
                        }
                    },
                    error: function() {
                        //currentInstructor.setCourses(JSON.parse('[{"id":3,"name":"TCR 101","crn":"22223","students":3},{"id":4,"name":"TCR 202","crn":"22223","students":3},{"id":5,"name":"TCR 303","crn":"22223","students":3},{"id":6,"name":"TCR 404","crn":"22223","students":3}]'))
                    }
                });
            }
        });

        if(location.pathname.substring(location.pathname.lastIndexOf("/") + 1) == "dashboard") {
            //GET USER INFO AND DISPLAY ON THE PAGE
            var Name = '';
            var profpic = '';
            $.ajax({
                url: '/user/auth',
                method: "GET",

                success: function(data){
                    var user = data.data.user
                    Name = user.name;

                    profpic = user.imageUrl;
                    var courseDiv = document.getElementById("userName");

                    var string = '<h2 class="section-heading">Hello, '+Name+'</h2>';
                    var div = document.createElement("div")
                    div.innerHTML = string;
                    courseDiv.appendChild(div);

                    var pic = document.getElementById("profilePic");
                    var picString ='<img src="'+profpic+'" class="img-circle" style="width: 5%">';

                    var profDiv = document.createElement("div");
                    profDiv.innerHTML = picString;
                    pic.appendChild(profDiv);


                }
            });
        }
    });
    
    function courseHTML(course, isInDeleteCoursesMode) {  
        var html = '<div id="course-item--{{course.id}}" class="col-md-4 col-sm-6 portfolio-item" style="box-shadow: 10px 10px 50px gray; padding: 10px;">'
            if (!isInDeleteCoursesMode) {
                html += '<a id="course-item-link--{{course.id}}" href="/course?courseId={{course.id}}" class="portfolio-link">'
            } else {
                html += '<div id="course-item-link--{{course.id}}">'
            }
            html += '<div class="portfolio-hover">'
                html += '<div class="portfolio-hover-content">'
                html += '<i class="fa fa-plus fa-3x"></i></div></div>'
                html += '<div class="portfolio-caption"><h4>{{course.name}}</h4><p class="text-muted"> CRN: {{course.crn}}</p>'
                if (isInDeleteCoursesMode) {
                    html+= '<button class="btn btn-danger js-deleteCourseButton" type="button" data-toggle="modal" data-target="#deleteCourseModal" data-course-id="{{course.id}}">Delete</button>'
                }
            html+= '</div>'
        html+= '</div>'
        return html
            .replaceAll('{{course.id}}', course.id)
            .replaceAll('{{course.name}}', course.name)
            .replaceAll('{{course.crn}}', course.crn)
    };

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

    function courseHTML(courseName, crn, id) {
        //var str = '<div class="col-md-4 col-sm-6 portfolio-item" style="box-shadow: 0px 0px 0px gray; padding: 20px;">'
        var str = '<div class="col-md-4 col-sm-6 portfolio-item" style="box-shadow: 10px 10px 50px gray; padding: 10px;">'
        //var str = '<div class="col-md-4 col-sm-6 portfolio-item" style="box-shadow: 0px 0px 0px gray; padding: 10px;">'
        str += '<a href="/course?courseId=' + id + ' class="portfolio-link" data-toggle="modal">'
            str += '<div class="portfolio-hover">'
            str += '<div class="portfolio-hover-content">'
            str += '<i class="fa fa-plus fa-3x"></i></div></div>'
            //str += '<asset:image class="img-responsive" src="logo.png" alt=""/>'
            //str += '<img src="assets/images/startup-framework.png" class="img-responsive" alt=""></a>'
        str += '<div class="portfolio-caption"><h4>' +courseName +'</h4><p class="text-muted"> CRN: '+ crn + '</p>'
        str+= '<button class="btn btn-danger js-deleteCourseButton" type="button" data-toggle="modal" data-target="#deleteCourseModal" data-course-id="' + id + '"></div></div>'
        return str
    };

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
            // alert("REMOVED STUDENT: " + JSON.stringify(student))
            window.location.reload()
        }, (err) => {
            alert(JSON.stringify(err))
        })
    });


    $('.js-deleteCourse').on('click', function() {
        currentInstructor.deleteCourseById($(this).data("course-id"), (course) => {
            //alert("DELETED COURSE BY ID: " + JSON.stringify(course))
        }, (err) => {
            alert(JSON.stringify(err))
        })
    });

    $('.js-enableDeleteCoursesMode').on('click', function() {
        currentInstructor.toggleDeleteCoursesMode(true)
        $(".js-enableDeleteCoursesMode").hide()
        $(".js-disableDeleteCoursesMode").show()
    });
    $('.js-disableDeleteCoursesMode').on('click', function() {
        currentInstructor.toggleDeleteCoursesMode(false)
        $(".js-enableDeleteCoursesMode").show()
        $(".js-disableDeleteCoursesMode").hide()
    });

    function prepareDeleteButton() {
        $('.js-deleteCourseButton').click(function () {
            const clickedButton = $(this);
            debugger
            const courseId = clickedButton.data('course-id');
            var course = currentInstructor.getCourseById(courseId);

            //clear existing data
            $('#deleteCourseModal').find('#confirmDeleteButton').data("course-id","");
            $('#deleteCourseModal').data("course-id", "");
            $('#deleteCourseModal').find('#courseId').html("");
            $('#deleteCourseModal').find('#courseName').html("")
            if (!course) {
                alert("Course not found by id " + courseId);
                return
            }
            debugger
            $('#deleteCourseModal').find('#confirmDeleteButton').data("course-id",course.id);
            $('#deleteCourseModal').data("course-id",  course.id);
            $('#deleteCourseModal').find('#courseId').html(course.id);
            $('#deleteCourseModal').find('#courseName').html(course.name)
        });
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


    studentDeleteButtonFormatter = function(_, student, index) {
        var deleteStudentButton = '<button class="btn btn-danger js-deleteStudentButton" type="button" data-toggle="modal" data-target="#deleteStudentModal" data-student-id="' + student.id + '">'
        deleteStudentButton += 'Delete'
        deleteStudentButton += '</button>'
        setTimeout(() => {
            prepareStudentDeleteButton()
        }, 500)
        return deleteStudentButton 
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
                        $('#coursePageTitle').html(course.name)
                    },
                    error: function() {
                        // currentInstructor.setCourses(JSON.parse('[{"id":3,"name":"TCR 101","crn":"22223","students":3},{"id":4,"name":"TCR 202","crn":"22223","students":3},{"id":5,"name":"TCR 303","crn":"22223","students":3},{"id":6,"name":"TCR 404","crn":"22223","students":3}]'))
                    
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

function changeDate(date) {
    console.log(date.value)
    updateDates(date.value)
};

function updateDates(_date) {
    console.log(currentInstructor)
    var _token
    var attendees = []
    currentInstructor.getTokenOrFetch((token) => {
                _token = token
            }, function(){alert("Error updating dates.")})
    if(_date) {
        $.ajax({
            url: '/api/course/attendance',
            data: {
                access_token: _token,
                course_id: courseId,
                date: _date
            },
            type: 'GET',
            async: false,
            success: function(stuff) {
                var _attendees = stuff.data.attendees
                console.log(_attendees)
                attendees = _attendees;
            },
            error: function(err) {
                // console.log(err);
            }
        });
    }
    console.log("TEST")
    console.log(attendees)
    $('#attendanceTable').bootstrapTable({
        data: attendees
    });
};