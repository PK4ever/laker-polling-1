var courses = [];
var token = '';

$(function() {
    $.ajax({
	    url: '/user/auth',
	    method: "GET",
	    success: function(data){
	    	token = data.data.token;
			var urlstr = '/api/course?=' + token;
		    $.ajax({
			    url: '/api/course',
			    method: "GET",
			    data: {
			    	access_token: token
			    },
			    success: function(data){
			    	courses = data.data.courses;
				    $('#courseTable').bootstrapTable({
				        data: courses
				    });
				}
			});
		}
	});
});

function identifierFormatter(value, row, index) {
    return [
            '<a class="like" href="javascript:void(0)" title="Like">',
                value,
            '</a>'].join('');
}