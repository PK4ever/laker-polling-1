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
        success: function(data) {
            console.log("POST question")
            console.log(data)
            refreshQuestions()
        },
        error: function(err){
            alert(err)
        }
    });
});

function refreshQuestions(){
console.log("REFRESH")
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
        },
        error: function(err){
            alert(JSON.stringify(err))
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
   