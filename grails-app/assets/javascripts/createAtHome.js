var token = '';
var quiz_id;
var courseId;

//get the instructor's token on load
$(function() {
    $.ajax({
        url: '/user/auth',
        method: "GET",
        success: function(data) {
            token = data.data.token;
        }
    });
});

$("#create-question-btn").click(function(){
    courseId = $(this).data('course-id')
    var selected = [];
    var answers = [];
    quiz_id = $(this).data('quiz-id')
    $(':checkbox').each(function(){
        if ($(this).is(':checked')){
            selected.push("true");
        }
        else{
            selected.push("false");
        }
    });
    question = $("#question-box")
    answers.push($("#answer-a").val());
    answers.push($("#answer-b").val());
    answers.push($("#answer-c").val());
    answers.push($("#answer-d").val());
    $.ajax({
        url: '/api/quiz/question?access_token=' + token + '&course_id=' + courseId + '&quiz_id=' + quiz_id + '&choices=' + answers.toString() + '&answers=' + selected.toString(),
        method: 'POST',
        success: refreshQuestions()
    });
});

function refreshQuestions(){
    var questions = [];
    $.ajax({
        url: '/api/quiz/question?access_token=' + token + '&quiz_id=' + quiz_id,
        method: 'GET',
        success: function(){
            var question_ids = data.questionIds
            for (var i = 0; i < question_ids.length; i++){
                $.ajax({
                    url:'/api/question?access_token=' + token + '&course_id=' + courseId + '&question_id' + question_ids[i],
                    method: 'GET',
                    success: questions.push(data.data);
                });
            }
        }
    });
    $('#question-table').bootstrapTable ({
        data: questions;
    })
}
   