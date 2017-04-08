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
	console.log(selected);
	var question_id = 1;
	var answer = selected.toString();

	$.ajax({
		url: '/api/question/answer?access_token=' + token + '&question_id=' 
			+ question_id + '&answer=' + answer + '&date=4/8/2017',
		type: 'PUT',
		// data: {
		// 	access_token: token,
		// 	question_id: 1,
		// 	answer: selected.toString()
		// },
		success: function(data) {
			console.log('It works');
		},
		error: function(err) {}

	});
});

