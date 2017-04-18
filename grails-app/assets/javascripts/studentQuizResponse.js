var token = '';
var question_id;
var course_id;
var quiz_id;
var answer;
var question_index;

// get student's access token on load
function getQuestion() {
    $.ajax({
        url: '/user/auth',
        method: "GET",
        success: function(data) {
            token = data.data.token;
            $.ajax({
                url: '/api/quiz/question?access_token=' + token + '&quiz_id=' + quiz_id,
                type: 'GET',
                success: function(data) {
                    console.log(quiz_id + 'is the quiz id');
                    var quiz_question_list = data.data.questionIds;
                    //Grab the first question in the list here. I'm assuming we still want to try and recurse through the
                    //questions by removing the first question from the list after the student answers it?
                    console.log(question_index)
                    console.log(quiz_question_list)
                    if(question_index>=quiz_question_list.length) {
                        window.location.href = "/course?courseId=" + course_id;
                    }
                    else {
                        var index = parseInt(question_index)
                        question_id = quiz_question_list[index];
                    }
                    $.ajax({
                        url: '/api/quiz/question?access_token=' + token + '&quiz_id=' + quiz_id + '&question_id=' + question_id,
                        type: 'GET',
                        success: function(data) {
                            console.log(question_id + 'is the q id');
                            question_id = data.data.id;
                            var question_text = data.data.text;
                            var choices = data.data.choices;
                            $('#question-text').html(question_text);
                            $('#answer0').html('A: ' + choices[0]);
                            $('#answer1').html('B: ' + choices[1]);
                            $('#answer2').html('C: ' + choices[2]);
                            $('#answer3').html('D: ' + choices[3]);
                            $('#answer4').html('E: ' + choices[4]);

                            //Not sure how to iterate through the answer buttons to replace the button labels with answers.
                            //Should we give each button a unique ID in the GSP and do it that way or will that mess something up?
                        }
                    });
                }
            });
        }
    });
}

$('.answer-btn').click(function() {
    $(this).toggleClass("answer-selected"); // change color of answer
});

$(':checkbox').change(function() { // just for testing, can be removed
    if ($(this).is(':checked')) {
    }
});


// STUDENT - submit answer
$("#submitAnswer").click(function() {
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

    var answer = selected.toString();
    console.log(answer)
    console.log(selected)
    // see if there's an active question
    $.ajax({
        url: '/api/quiz/question/answer?access_token=' + token + '&quiz_id=' + quiz_id +'&question_id=' + question_id + '&answer=' + answer,
        type: 'PUT',
        success: function() {
            question_index++
            alert("Answer accepted")
            window.location.href = "/course/quiz?courseId=" + course_id + "&quizId=" + quiz_id + "&questionIndex=" + question_index;
        },
        error: authFailure
    });
});

function authFailure(xhr, textStatus, errorThrown){
    var responseText = xhr.responseText;
    if(responseText != null) {
        var status;
        try {
            status = JSON.parse(responseText);
        }
        catch(e) {
            status = undefined;
        }
        if(status && status.message) {
            alert(status.message);
        }
    }
}


function prepareClassTitle(cId) {
    course_id = cId;
}

function prepareQuizTitle(qId) {
    quiz_id = qId;
}

function prepareQuestionIndex(qIndex) {
    question_index = qIndex;
    getQuestion()
}



////Instructor Submit Question
//$("#submit-question-btn").click(function(){
//    var courseId = $(this).data('course-id')
//    var selected = [];
//    $(':checkbox').each(function(){
//        if ($(this).is(':checked')){
//            selected.push("true");
//        }
//        else{
//            selected.push("false");
//        }
//    });
//    $.ajax({
//        url: '/api/question?access_token=' + token + '&course_id=' + courseId + '&answers=' + selected.toString(),
//        method: 'POST',
//        success: function(data){
//            question_id = data.id
//            $.ajax({
//                url: '/api/question?access_token=' + token + '&question_id=' + question_id + '&flip=true',
//                method: 'PUT',
//                success: toggleButtons()
//            });
//        },
//        error: function(err){
//            alert(JSON.stringify(err))
//        }
//    });
//});
//
////
//$("#close-question-btn").click(function(){
//    $.ajax({
//        url: '/api/question?access_token=' + token + '&question_id=' + question_id + '&flip=false',
//        method: 'PUT',
//        success: toggleButtons()
//    });
//})
//
////Make close and start buttons appear/disappear
//function toggleButtons (){
//
//    var linkdiv = document.getElementById("resultLink")
//    var string = '<a href="viewresults?courseId='+courseId+'&questionId='+question_id+'" class="btn btn-default" id="show-results-btn">Show Results</a>'
//    var div = document.createElement("div")
//    div.innerHTML=string
//    linkdiv.appendChild(div)
//
//    var close = document.getElementById('close-question-btn')
//    var question = document.getElementById('question-form')
//    var showResults = document.getElementById('show-results-btn')
//    if (close.style.display === 'none'){
//        close.style.display = 'inline-block'
//        question.style.display = 'none'
//        showResults.style.display = 'none'
//    } else {
//        question.style.display = 'inline-block'
//        showResults.style.display = 'inline-block'
//        close.style.display = 'none'
//    }
//}

//  $(document).ready(function() {
//      var linkdiv = document.getElementById("resultLink")
//      var string = '<a href="viewresults?courseId='+courseId+'&questionId='+question_id+'" class="btn btn-default" id="show-results-btn">Show Results</a>'
//      var div = document.createElement("div")
//      div.innerHTML=string
//      linkdiv.appendChild(div)
//  })

