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

// $('.checkbox').change(function() {
//     $(this).toggleClass("answer-selected"); // change color of answer
// })

$("#submitAnswer").click(function() {
	$.ajax({
		url: '/api/question/answer',
		data: {
			access_token: token,

		},
		success: function(data) {

		}

	});
});

