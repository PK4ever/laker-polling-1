var token = '';
var quiz_id;
var courseId;
var deleteQuestionButtonFormatter;

//get the instructor's token on load
$(function() {
    $.ajax({
        url: '/user/auth',
        method: "GET",
        success: function(data) {
            token = data.data.token;
            refreshQuestions()
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
    question = $("#question-text").val()
    answers.push($("#answer-a").val());
    answers.push($("#answer-b").val());
    answers.push($("#answer-c").val());
    answers.push($("#answer-d").val());
    answers.push($("#answer-e").val());
    //console.log(question)
    var urlStr = '/api/quiz/question?access_token=' + token + '&quiz_id=' + quiz_id + '&text=' + question +'&choices=' + answers.toString() + '&answers=' + selected.toString()
    //console.log(urlStr)
    $.ajax({
        url: urlStr,
        method: 'POST',
        success: window.location.reload()
    });
});

function setQuizId(id){
    quiz_id = id
}

function refreshQuestions(){
//console.log("REFRESH")4
    var questions = [];
    $.ajax({
        url: '/api/quiz/question?access_token=' + token + '&quiz_id=' + quiz_id,
        method: 'GET',
        success: function(data){
            var question_ids = data.data.questionIds
            console.log(question_ids)
            for (var i = 0; i < question_ids.length; i++){
                console.log("i="+i)
                $.ajax({
                    url:'/api/quiz/question?access_token=' + token + '&quiz_id=' + quiz_id + '&question_id=' + question_ids[i],
                    method: 'GET',
                    success: function(data){
                        console.log("Inside GET question")
                        console.log(data)
                        questions.push(data.data)
                        if(questions.length == question_ids.length) {
                            setTableData(questions)
                        }
                    }
                });
            }

        }
    });
}

var preparedDeleteQuestion = false
function prepareDeleteQuestionButton() {
    if(preparedDeleteQuestion) return;
    $('.js-deleteQuestionButton').click(function () {
        const clickedSButton = $(this)
        const qId = clickedSButton.data('question-id');
        $('#deleteQuestionModal').find('#confirmDeleteQuestion').data("question-id", qId);
    })
    prepareDeleteQuestion = true;
}

deleteQuestionButtonFormatter = function(_, question, index) {
    var deleteQuestionButton = '<button class="btn btn-danger js-deleteQuestionButton" type="button" data-toggle="modal" data-target="#deleteQuestionModal" data-question-id="' + question.id + '">'
    deleteQuestionButton += 'Delete'
    deleteQuestionButton += '</button>'
    setTimeout(() => {
        prepareDeleteQuestionButton()
    }, 500)
    return deleteQuestionButton
}

$('.js-deleteQuestion').click(function () {
    $.ajax({
        url: '/api/quiz/question?access_token=' + token + '&quiz_id=' + quiz_id + '&question_id=' + $(this).data("question-id"),
        method: "DELETE",
        success: function(){
            window.location.reload()
        }
    });
});


$('#backButton').on('click', function(event) {
    $.ajax({
        url: '/user/auth',
        type: 'GET',
        success: function(data) {
            var token = data.data.token;
            debugger
            if (confirm('You are about to return to the quiz list. This quiz will be deleted if you proceed. Are you sure you wish to continue?')) {
                $.ajax({
                    url: '/api/quiz/?access_token=' + token + '&quiz_id=' + quiz_id,
                    type: 'DELETE',
                    success: function(data) {
                        window.location.href = "/course/quizList?courseId=" + courseId;
                    },
                    error: function(jqXHR, textStatus, errorMessage) {
                        console.log(errorMessage)
                    }

                });
            } else {
                return;
            }
        }
    });
});

function setTableData(questions) {
    console.log("SET TABLE DATA")
    console.log(questions)
    $('#question-table').bootstrapTable ({
       data: questions
    })
}
   