var token = '';
var question_id;
var answer;

// get student's access token on load
$(function() {
    $.ajax({
        url: '/user/auth',
        method: "GET",
        success: function(data) {
            token = data.data.token;
        }
    });
});

$('.answer-btn').click(function() {
    $(this).toggleClass("answer-selected"); // change color of answer
});

$(':checkbox').change(function() { // just for testing, can be removed
    if ($(this).is(':checked')) {
    }
});


// STUDENT - submit answer
$("#submitAnswerLive").click(function() {
        var courseId = $(this).data('course-id');
        var selected = [];
        $(':checkbox').each(function() {
            if ($(this).is(':checked')) {
                selected.push("true");
            }
            else {
                selected.push("false");
            }
        });
        // console.log(selected.toString());


    var question_id;
    var answer = selected.toString();
    // see if there's an active question
    $.ajax({
        url: '/api/question/active?access_token=' + token + '&course_id=' + courseId,
        type: 'GET',
        success: function(data) {
            console.log(data)
            question_id = data.questionId; // get question_id
            $.ajax({
                url: '/api/question/answer?access_token=' + token + '&question_id=' + question_id + '&answer=' + answer,
                type: 'PUT',
                success: function() {
                    console.log(question_id + 'is the q id')
                    alert('Answer submitted!')
                    window.location.href="/course/answerquestion?courseId=" + courseId
                },
                error: function() {
                    alert('This question is no longer active!');
                    window.location.href="/course?courseId=" + courseId
                }
            });
        },
        error: function() {
            alert('This question is not ready, please wait for instructor and try again.');
        }
    });
});



//Instructor Submit Question
$("#submit-question-btn").click(function(){
    debugger
    var courseId = $(this).data('course-id')
    var selected = [];
    $(':checkbox').each(function(){
        if ($(this).is(':checked')){
            selected.push("true");
        }
        else{
            selected.push("false");
        }
    });
    $.ajax({
        url: '/api/question?access_token=' + token + '&course_id=' + courseId + '&answers=' + selected.toString(),
        method: 'POST',
        success: function(data){
            question_id = data.id
            $.ajax({
                url: '/api/question?access_token=' + token + '&question_id=' + question_id + '&flip=true',
                method: 'PUT',
                success: toggleButtons()
            });
        },
        error: function(err){
            alert(JSON.stringify(err))
        }
    });
});

//Instructor Submit Question
$("#submit-question-btn2").click(function(){
    var courseId = $(this).data('course-id')
    var selected = [];
    $(':checkbox').each(function(){
        if ($(this).is(':checked')){
            selected.push("true");
        }
        else{
            selected.push("false");
        }
    });
    $.ajax({
        url: '/api/question?access_token=' + token + '&course_id=' + courseId + '&answers=' + selected.toString(),
        method: 'POST',
        success: function(data){
            question_id = data.id
            $.ajax({
                url: '/api/question?access_token=' + token + '&question_id=' + question_id + '&flip=true',
                method: 'PUT',
                success: toggleButtons()
            });
        },
        error: function(err){
            alert(JSON.stringify(err))
        }
    });
});

//
$("#close-question-btn").click(function(){
    $.ajax({
        url: '/api/question?access_token=' + token + '&question_id=' + question_id + '&flip=false',
        method: 'PUT',
        success: toggleButtons(),
        error: function(){
            $.ajax({
                url: '/api/question/active?access_token=' + token + '&course_id=' + $(this).data('course-id'),
                method: 'GET',
                success: function (data){
                    question_id = data.questionId
                    $.ajax({
                        url: '/api/question?access_token=' + token + '&question_id=' + question_id + '&flip=false',
                        method: 'PUT',
                        success: toggleButtons()
                    });
                }
            });
        }
    });
});

//Make close and start buttons appear/disappear
function toggleButtons (){

    var linkdiv = document.getElementById("resultLink")
    var string = '<a href="viewresults?courseId='+courseId+'&questionId='+question_id+'" class="btn btn-default" id="show-results-btn">Show Results</a>'
    var div = document.createElement("div")
    div.innerHTML=string
    linkdiv.appendChild(div)

    var close = document.getElementById('close-question-btn')
    var question = document.getElementById('question-form')
    var submit1 = document.getElementById('submit-question-btn')
    var submit2 = document.getElementById('submit-question-btn2')
    var showResults = document.getElementById('show-results-btn')
    var checkboxDiv = document.getElementById('checkboxDiv')

    if (close.style.display === 'none'){
        close.style.display = 'inline-block'
        question.style.display = 'none'
        showResults.style.display = 'none'
    } else {
        question.style.display = 'inline-block'
        submit1.style.display = 'none'
        submit2.style.display = 'inline-block'
        showResults.style.display = 'inline-block'
        close.style.display = 'none'
        var str = '<label class="btn btn-default" id="answers">'
        str += '<input type="checkbox" autocomplete="off" name="vehicle" id="checkbox1">A</label><br>'
        str += '<label class="btn btn-default" id="answers">'
        str += '<input type="checkbox" autocomplete="off" name="vehicle" id="checkbox2">B</label><br>'
        str += '<label class="btn btn-default" id="answers">'
        str += '<input type="checkbox" autocomplete="off" name="vehicle" id="checkbox3">C</label><br>'
        str += '<label class="btn btn-default" id="answers">'
        str += '<input type="checkbox" autocomplete="off" name="vehicle" id="checkbox4">D</label><br>'
        str += '<label class="btn btn-default" id="answers">'
        str += '<input type="checkbox" autocomplete="off" name="vehicle" id="checkbox5">E</label><br>'
        checkboxDiv.innerHTML = str
           
    }
}

//  $(document).ready(function() {
//      var linkdiv = document.getElementById("resultLink")
//      var string = '<a href="viewresults?courseId='+courseId+'&questionId='+question_id+'" class="btn btn-default" id="show-results-btn">Show Results</a>'
//      var div = document.createElement("div")
//      div.innerHTML=string
//      linkdiv.appendChild(div)
//  })

