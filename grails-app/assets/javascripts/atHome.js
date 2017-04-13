var courseId;


$(function(){
	$.ajax({
        url: '/user/auth',
        method: "GET",
        success: function(data){
            var token = data.data.token
            console.log(token)
            $.ajax({
                url: '/api/quiz?access_token=' +  token + '&course_id=' + courseId,
                method: "GET",
                success: function(data) {
                	var quizzes = data.data.quizzes
                    $('#quizTable').bootstrapTable({
		                data: quizzes
		            });
                }
            });
        }
    });
});

$('#quiz-form').on('click', function(event) {
        console.log("TEST");
        $.ajax({
            url: '/user/auth',
            type: 'GET',
            success: function(data) {
                var token = data.data.token;
                var name = $("#quizName").val();
                var start = $("#startDate").val();
                var end = $("#endDate").val();

                console.log(name);
                console.log(start);
                console.log(end);

                $.ajax({
                    url: '/api/course/student?access_token=' + token + '&course_id=' + courseId + '&name=' + name + '&start_time=' + start + '&end_time=' + end,
                    type: 'POST',
                    success: function(data) {
                        window.location.href = "/createQuiz?courseId=" + courseId + "&quizId=" + data.data.quiz.id;
                    },
                    error: function(jqXHR, textStatus, errorMessage) {
                        console.log(errorMessage)
                    }

                });
            }
        });
    });

function prepareClassTitle(cId) {
    courseId = cId;
}