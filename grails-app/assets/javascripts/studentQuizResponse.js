var token = '';
var question_id;
var course_id;
var quiz_id;
var answer;
var question_index;
var quiz_question_list;

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
                    quiz_question_list = data.data.questionIds.sort();
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
            if(question_index < quiz_question_list.length) {
                window.location.href = "/course/quiz?courseId=" + course_id + "&quizId=" + quiz_id + "&questionIndex=" + question_index;
            }
            else {
                //submit quiz
                $.ajax({
                    url: '/api/quiz/submission?access_token=' + token + '&quiz_id=' + quiz_id,
                    type: 'POST',
                    success: function(data) {
                        console.log("success")
                    },
                    error: function() {
                        console.log("error")
                    }
                });
                window.location.href = "/course?courseId=" + course_id
            }
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
