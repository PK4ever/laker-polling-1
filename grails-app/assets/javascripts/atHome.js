var courseId;
var deleteQuizFormatter;
var token;


$(function(){
	$.ajax({
        url: '/user/auth',
        method: "GET",
        success: function(data){
            token = data.data.token
            console.log(token)
            $.ajax({
                url: '/api/quiz?access_token=' +  token + '&course_id=' + courseId,
                method: "GET",
                success: function(data) {
                	var quizzes = data.data.quizzes
                    console.log(quizzes)
                    $('#quizTable').bootstrapTable({
		                data: quizzes
		            });
                }
            });
        }
    });
});

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


            // Moment.js takes care of setting dates and converting them to Unix milliseconds
            var startTimestamp = moment().valueOf(startDate + ' ' + startTime)
            var endTimestamp = moment().valueOf(endDate + ' ' + endTime)

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
    return [
        '<a class="like" href="/course/quiz?courseId=' + courseId + '&quizId=' + row.id + '&questionIndex=0" title="Like">',
        value,
        '</a>'].join('');
}

function dateFormatter(value, row, index) {
    var str = value.split('T')
    var date = str[0]
    var time = str[1].substring(0,str[1].length-1)
    return date + ' :: ' + time;
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