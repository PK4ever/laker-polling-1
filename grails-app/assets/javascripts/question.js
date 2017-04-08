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

$(':checkbox').change(function() {
	if ($(this).is(':checked')) {
		console.log("Checked");
	}
});

$("#submitAnswer").click(function() {
	var form = $('#answer-form');
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

	// $.ajax({
	// 	url: '/api/question/answer',
	// 	data: {
	// 		access_token: token,

	// 	},
	// 	success: function(data) {

	// 	}

	// });
});

