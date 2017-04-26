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

        this.getQuizGradesById = function(id, onSuccess, onFail) {
            if (id == null) return;
            _instructor.getTokenOrFetch((token) => {

                var urlString = '/api/quiz/grades?access_token=' + token + '&quiz_id=' + id;
                NetworkUtils.runAjax(urlString, 'GET', function(data){
                    if (!ArrayUtils.isArray(data.data.grades)) {
                        return onFail(new Error("Could not find grades by that "))
                    }
                    onSuccess(data.data.grades)
                }, function(err){
                    onFail(err)
                })
            }, onFail)
        }

        this.getQuestionPerformanceByDate = function(date, course_id, onSuccess, onFail){
            if (date == null || course_id == null) return;
            _instructor.getTokenOrFetch((token) => {

                var urlString = '/api/question/result?access_token=' + token + '&course_id=' + course_id + '&date=' + date;
                NetworkUtils.runAjax(urlString, 'GET', function(data){
                    if(!ArrayUtils.isArray(data.data.results)){
                        return onFail(new Error ("No questions found for that date"))
                    }
                    console.log(data.data)
                    onSuccess(data.data)
                }, function(err){
                    onFail(err)
                })
            }, onFail)
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
        }

        this.getCourses = function() {
            return _courses
        }

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

        this.refreshQuizGradesTableById = function(quizId){
            
            const html = '<table class="table">\
                <thead>\
                <tr>\
                    <th class="col-md-1">Quiz {{quizId}} Results</th>\
                </tr>\
                {{dynamicTableRows}}\
                {{dynamicDownloadButtonRow}}\
                </thead>\
            </table>'
            _service.getQuizGradesById(quizId, (studentGrades) => {
                _service.getToken((accessToken) => {
                    const tableRowHTML = "<tr><td>{{name}}</td><td>{{grade}}</td></tr>"
                    const dynamicDownloadButtonRowHTML = '<tr><td><a href="/api/quiz/file/grades?access_token={{accessToken}}&course_id=' + courseId + '">\
                        <button class="btn" type="button">Download CSV File for the class</button>\
                    <\a></td></tr>'
                    var dynamicTableRowsHTML = ''
                    ArrayUtils.forEachCachedLength(studentGrades, (grade) => {
                        dynamicTableRowsHTML += tableRowHTML.replaceAll('{{name}}', grade.name).replaceAll('{{grade}}', (grade.grade * 100) + "%")
                    })
                    $('#quizGradesTablesContainer').html(
                        html.replaceAll('{{quizId}}', quizId)
                            .replace('{{dynamicTableRows}}', dynamicTableRowsHTML)
                            .replace('{{dynamicDownloadButtonRow}}', dynamicDownloadButtonRowHTML)
                            .replace('{{accessToken}}', accessToken)
                    )
                })
                
            }, (err) => {
                $('#quizGradesTablesContainer').html(
                    html
                    .replace('{{quizId}}', quizId)
                    .replace('{{dynamicTableRows}}', '<tr><td>No Grades Available</td></tr>')
                    .replace('{{dynamicDownloadButtonRow}}', '')
                )
            })
        }

        this.refreshQuestionResponsesTableByDate = function (date){
            var index = 0
            const html = '<table class="table">\
                <thead>\
                <tr>\
                    <th class="col-md-1">Question</th>\
                    <th class="col-md-1">A</th>\
                    <th class="col-md-1">B</th>\
                    <th class="col-md-1">C</th>\
                    <th class="col-md-1">D</th>\
                    <th class="col-md-1">E</th>\
                    <th class="col-md-2">Correct Answer</th>\
                    <th class="col-md-2">% Correct</th>\
                </tr>\
                </thead>\
                {{dynamicTableRows}}\
                </table>'

                _service.getQuestionPerformanceByDate(date, courseId, (responses) => {
                    const tableRowHTML = "<tr><td>{{index}}</td>\
                    <td>{{numA}}</td><td>{{numB}}</td><td>{{numC}}</td>\
                    <td>{{numD}}</td><td>{{numE}}</td><td>{{correct}}</td><td>{{pc}}</td></tr>"

                    var dynamicTableRowsHTML = ''
                    ArrayUtils.forEachCachedLength(responses.results, (question) => {
                        dynamicTableRowsHTML += tableRowHTML.replaceAll('{{index}}', index)
                            .replaceAll('{{numA}}', question.answers[0])
                            .replaceAll('{{numB}}', question.answers[1])
                            .replaceAll('{{numC}}', question.answers[2])
                            .replaceAll('{{numD}}', question.answers[3])
                            .replaceAll('{{numE}}', question.answers[4])
                            .replaceAll('{{correct}}', question.correct)
                            .replaceAll('{{pc}}', question.percentCorrect)
                        index++
                    })
                    $('#questionPerformanceContainer').html(
                        html.replace('{{dynamicTableRows}}', dynamicTableRowsHTML)
                    )
                }, (err) => {
                    $('#questionPerformanceContainer').html(html.replace('{{dynamicTableRows}}', ''))
                })
        }

        this.toggleDeleteCoursesMode = function(enabled) {
            _isInDeleteCoursesMode = !!enabled
            this.refreshCourseTable()
        }

        this.isInDeleteCoursesMode = function() {
            return _isInDeleteCoursesMode
        }

        this.refreshQuizGradesTableById(NetworkUtils.getCurrentLocationQueryParam('quizId'))

        this.refreshQuestionResponsesTableByDate(NetworkUtils.getCurrentLocationQueryParam('date'))
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
  
    $('#courseButton').on('click', function(event) {
        event.preventDefault();
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
                    success: function(ev) {
                        document.location.href = "/dashboard";
                    }
                })
            }

        });
    });

    $('.js-createCourse').click( function(){
        $.ajax({
            url: '/user/auth',
            method: 'GET',
            success: function(data){
                var token = data.data.token
                var courseName = $('#modalCourseName').val()
                var CRN = $('#modalCourseCRN').val()
                if (courseName == null || CRN == null) alert('Please enter a name and CRN')
                var urlStr = '/api/course?access_token=' + token + '&name=' + courseName + '&crn=' + CRN
                $.ajax({
                    url: urlStr,
                    method:'POST',
                    success: function(data){
                        alert(courseName + "created!")
                        window.location.reload()
                    },
                    error: function(){
                        alert("An error occurred, please try again.")
                    }
                });
            }
        });

    })

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
        event.preventDefault();
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
//                        console.log('Works');
                         window.location.reload();
                    },
                    error: function(jqXHR, textStatus, errorMessage) {
                        console.log(errorMessage)
                    }

                });
            }
        });
    });

    $('.js-createInstructor').click(function() {
        $.ajax({
            url: '/user/auth',
            method: 'GET',
            success: function(data){
                var token = data.data.token
                var email = $('#instEmail').val()
                if (email == null) alert('Please enter a SUNY Oswego email')
                var urlStr = '/api/user/role?access_token=' + token + '&email=' + email + '&master=INSTRUCTOR'
                $.ajax({
                    url: urlStr,
                    method:'PUT',
                    success: function(data){
                        alert("User: " + email + "is now an instructor")
                    },
                    error: function(){
                        alert("An error occurred, please try again.")
                    }
                });
            }
        });

    })

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
            // debugger
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
            // debugger
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
    

    function prepareClassTitle(courseId) {
        $.ajax({
            url: '/user/auth',
            method: "GET",
            success: function(data){
                var token = data.data.token
                var sessionx = session
                var quizId = sessionx.quizId
                debugger
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

$('#roleButton').on('click', function(event) {
    $.ajax({
        url: '/user/auth',
        type: 'GET',
        success: function(data) {
            var token = data.data.token;
            var userId = data.data.user.id
            $.ajax({
                url: '/api/user/role?access_token='+token+'&current=STUDENT',
                type: 'PUT',
                success: function(data) {
                    window.location.href = "/dashboard";
                },
                error: function(jqXHR, textStatus, errorMessage) {
                    console.log(errorMessage)
                }

            });
        }
    });
});

function prepareClassTitle(cId) {
    courseId = cId;
}

function changeDate(date) {
    console.log(date);
    updateDates(date);

};

function updateDates(_date) {
    // console.log(currentInstructor)
    var _token
    var attendees = [];

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
                // attendees = _attendees;
                if (_attendees == null) {
                    console.log('u got nothin')
                    $('#attendanceTable').bootstrapTable('removeAll');
                    
                } else {
                    $('#attendanceTable').bootstrapTable('load', _attendees);
                    
                }
                $('#attendanceTable').bootstrapTable('refresh');

                // $("#date").val(""); // clear the picker

            },
            error: function(err) {
                // console.log(err);
            }
        });
    }
};

function getQuestionsFor(date, course_id){
    courseId = course_id
    currentInstructor.refreshQuestionResponsesTableByDate(date)
}



