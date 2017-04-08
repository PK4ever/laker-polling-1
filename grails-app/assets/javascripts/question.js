var token = '';
var question_id;
var answer;
var toggleButtons;

// get student's access token
$(function() {
    $.ajax({
	    url: '/user/auth',
	    method: "GET",
	    success: function(data){
	    	token = data.data.token;
		}
	});
});		    

$('.answer-btn').click(function() {
    $(this).toggleClass("answer-selected"); // change color of answer
});

$(':checkbox').change(function() { // just for testing
	if ($(this).is(':checked')) {
	}
});


// STUDENT - submit answer
$("#submitAnswer").click(function() {
	var selected = [];
	$(':checkbox').each(function() {
		if ($(this).is(':checked')) {
    		selected.push("true");
		}
		else {
			selected.push("false");
		}
	});
	console.log(selected.toString());
	var question_id = 1;
	var answer = selected.toString();
	$.ajax({
		url: '/api/question/answer?access_token=' + token + '&question_id=' 
 			+ question_id + '&answer=' + answer,
 		type: 'PUT',
 		success: function() {
 			console.log('it works')
 		}
	});
});



//Instructor Submit Question
$("#submit-question-btn").click(function(){
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
    debugger
});

$("#close-question-btn").click(function(){
    $.ajax({
        url: '/api/question?access_token=' + token + '&question_id=' + question_id + '&flip=false',
        method: 'PUT',
        success: toggleButtons()
    });
})

//Make close and start buttons appear/disappear
 toggleButtons = function(){
    var close = document.getElementById('close-question-btn')
    var question = document.getElementById('question-form')
    if (close.style.display === 'none'){
        close.style.display = 'block'
        question.style.display = 'none'
    } else {
        question.style.display = 'block'
        close.style.display = 'none'
    }
    debugger
 }


