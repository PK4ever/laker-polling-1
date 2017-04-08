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

	$.ajax({
		url: '/api/question/answer',
		type: 'POST',
		data: {
			access_token: token,
			question_id: 2,
			answer: selected.toString()

		},
		success: function(data) {
			console.log('It works');
		},
		error: function(err) {}

	});
});

