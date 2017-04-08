var token = '';
var question_id;
var answer;

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

$("#submit-question-btn").click(function(){
    var form = $('#question-form')
    var courseId = $(this).data('course-id')
    var selected = ''
    $(':checkbox').each(function(){
        if ($(this).is(':checked')){
            selected = selected + 'true';
        }
        else{
            selected.push("false");
        }
        console.log(selected.toString())
    });
    // debugger
    $.ajax({
        url: '/api/question?access_token=' + token + '&course_id=' + courseId + '&answers=' + selected,
        method: 'POST',
        success: function(data){
            question_id = data.id
            console.log("IT WORKED!!!!")
        },
        error: function(err){
            alert(JSON.stringify(err))
        }
    })
});


