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

$('#newQuizButton').on('click', function(event) {
    $.ajax({
        url: '/user/auth',
        type: 'GET',
        success: function(data) {
            var token = data.data.token;
            var name = $("#quizName").val();
            var startDate = $("#startDate").val();
            var startTime = $("#startTime").val();
            var endDate = $("#endDate").val();
            var endTime = $("#endTime").val();
            
            var startTimestamp = Date.parse(startDate + ' ' + startTime)
            var endTimestamp = Date.parse(endDate + ' ' + endTime)

            //var urlStr = '/api/quiz?access_token=' + token + '&course_id=' + courseId + '&name=' + name + '&start_timestamp=' + startTimestamp + '&end_timestamp=' + endTimestamp
            if(!name || !startTimestamp || !endTimestamp) {alert("Please fill all fields for quiz creation.");return;}
            
            $.ajax({
                url: '/api/quiz?access_token=' + token + '&course_id=' + courseId + '&name=' + name + '&start_timestamp=' + startTimestamp + '&end_timestamp=' + endTimestamp,
                type: 'POST',
                success: function(data) {
                    window.location.href = "/course/createQuiz?courseId=" + courseId + "&quizId=" + data.data.students.id;
                },
                error: function(jqXHR, textStatus, errorMessage) {
                    console.log(errorMessage)
                }

            });
        }
    });
});

function identifierFormatter(value, row, index) {
    return [
        '<a class="like" href="/course/quiz?courseId=' + courseId + '&quizId=' + row.id + '&questionIndex=0" title="Like">',
        value,
        '</a>'].join('');
}

function prepareClassTitle(cId) {
    courseId = cId;
}