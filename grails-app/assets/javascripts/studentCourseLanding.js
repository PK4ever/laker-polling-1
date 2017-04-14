var courseId;

$(function(){
	$.ajax({
        url: '/api/question/active?access_token=' + token + '&course_id=' + courseId,
        type: 'GET',
        success: function(data) {
            document.getElementsByName('livePollButton').style.visibility="visible";
            document.getElementsByName('livePollMessage').style.visibility="hidden";
        },
        error: function() {
            document.getElementsByName('livePollButton').style.visibility="hidden";
            document.getElementsByName('livePollMessage').style.visibility="visible";
        }
    });
}

function prepareClassTitle(cId) {
    courseId = cId;
}