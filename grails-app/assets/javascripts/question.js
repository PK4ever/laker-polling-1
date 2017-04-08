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

$("#submit-question-btn").click(function(){
    var form = $('#question-form')
    var courseId = $(this).data('course-id')
    var selected = ''
    $(':checkbox').each(function(){
        if ($(this).is(':checked')){
            selected = selected + 'true');
        }
        else{
            selected.push("false");
        }
        console.log(selected)
    });
    debugger
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

	// $.ajax({
	// 	url: '/api/question/answer',
	// 	data: {
	// 		access_token: token,

	// 	},
	// 	success: function(data) {

	// 	}

	// });

