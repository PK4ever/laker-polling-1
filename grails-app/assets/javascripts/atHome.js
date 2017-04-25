var courseId;
var deleteQuizFormatter;
var token;
var isInstructor;


$(function(){
	$.ajax({
        url: '/user/auth',
        method: "GET",
        success: function(data){
            token = data.data.token
            console.log(token)
            if(data.data.user.type == "INSTRUCTOR") isInstructor = true;
            else isInstructor = false;
            $.ajax({
                url: '/api/quiz?access_token=' +  token + '&course_id=' + courseId,
                method: "GET",
                success: function(data) {
                	var quizzes = data.data.quizzes
                    console.log(quizzes)
                    if(isInstructor) {
                        $('#quizTable').bootstrapTable({
                            data: quizzes
                        });
                        return
                    }
                    checkStudentSubmissionStatusForQuizzes(quizzes)
                        .always(() => {
                            $('#quizTable').bootstrapTable({
                                data: quizzes
                            });
                        })
                }
            });
        }
    });
});

function checkStudentSubmissionStatusForQuizzes(quizzes) {
    const promises = quizzes.map((quiz) => {
        return checkStudentQuizSubmissionStatus(quiz)
    })
    return $.when.apply($, promises)
}

function checkStudentQuizSubmissionStatus (quiz) {
    var deferred = $.Deferred()
    $.ajax({
        url: '/api/quiz/submission?access_token=' + token + '&quiz_id=' + quiz.id,
        type: 'GET',
        success: function(data) {
            //console.log("success")
            quiz.alreadySubmitted = true
            deferred.resolve(true)
        },
        error: function(error) {
            quiz.alreadySubmitted = false
            deferred.resolve(false)
        }
    });
    return deferred.promise()
}

$('#newQuizButton').on('click', function(event) {
    $.ajax({
        url: '/user/auth',
        type: 'GET',
        success: function(data) {
            var token = data.data.token;
            var name = $("#quizName").val();
            var startDate = $("#startDate").val();
            var startTime = $("#startTime").val();
            var endDate = $("#endDate").val();
            var endTime = $("#endTime").val();

            debugger

            // Moment.js takes care of setting dates and converting them to Unix milliseconds
            var startTimestamp = moment(startDate + ' ' + startTime).utc();
            var endTimestamp = moment(endDate + ' ' + endTime).utc();


            var urlStr = '/api/quiz?access_token=' + token + '&course_id=' + courseId + '&name=' + name + '&start_timestamp=' + startTimestamp + '&end_timestamp=' + endTimestamp
            if(!name || !startTimestamp || !endTimestamp) {
                alert("Please fill all fields for quiz creation.");
                return;
            }

            if(startTimestamp > endTimestamp) {
                alert("End time must be after start time.");
                return;
            }
            $.ajax({
                url: urlStr,
                type: 'POST',
                success: function(data) {
                    window.location.href = "/course/createQuiz?courseId=" + courseId + "&quizId=" + data.data.students.id;
                },
                error: function(jqXHR, textStatus, errorMessage) {
                console.log(startTimestamp);

                    alert("An error occurred while making the quiz. Make sure you did not set your start/end dates in the past!")
                    console.log(errorMessage)
                }

            });
        }
    });
});

function identifierFormatter(value, row, index) {
    if (row.alreadySubmitted && !isInstructor) {
        return value + '<br><span style="font-size:10px">Submitted</span>'
    } else {
        if(!isInstructor) {
            return '<a class="like" href="/course/quiz?courseId=' + courseId + '&quizId=' + row.id + '&questionIndex=0" title="Like">' + value + '</a>'
        } else {
            return '<a class="like" href="/course/quiz/grades?courseId=' + courseId + '&quizId=' + row.id + '" title="Like">' + value + '</a>'
        }
    }
}

function dateFormatter(value, row, index) {
    var result = moment(value).tz('America/New_York').toString();
    return result.substring(0, result.length - 12);
}

$('.js-deleteQuiz').click(function(){
    $.ajax({
        url: '/api/quiz?access_token=' + token + '&quiz_id=' + $(this).data("quiz-id"),
        method: 'DELETE',
        success: window.location.reload()
    });
});

var prepareDeleteQuiz = false
function prepareDeleteQuizButton() {
    if(prepareDeleteQuiz) return;
    $('.js-deleteQuizButton').click(function () {
        const clickedButton = $(this)
        const quiz_Id = clickedButton.data('quiz-id');
        $('#deleteQuizModal').find('#confirmDeleteQuiz').data("quiz-id", quiz_Id);
    })
    prepareDeleteQuiz = true;
}

deleteQuizFormatter = function(_, quiz, index){
    var deleteQuestionButton = '<button class="btn btn-danger js-deleteQuizButton" type="button" data-toggle="modal" data-target="#deleteQuizModal" data-quiz-id="' + quiz.id + '">'
    deleteQuestionButton += 'Delete'
    deleteQuestionButton += '</button>'
    setTimeout(() => {
        prepareDeleteQuizButton()
    }, 500)
    return deleteQuestionButton
}

function prepareClassTitle(cId) {
    courseId = cId;
}