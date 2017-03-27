var token = '';
var courses = [];

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


$('#courseButton').on('click', function() {
    console.log('Clicked');
    $.ajax({
        url: '/user/auth',
        method: 'GET',
        success: function(data) {
            token = data.data.token;

            var courseName = $('#courseName').val();
            var courseCRN = $('#courseCRN').val();
            var urlstring = '/api/course?access_token=' + token + '&name=' + courseName + '&crn=' + courseCRN;
            console.log(courseName);
            console.log(courseCRN);
            console.log(urlstring);

            $.ajax({
                url: '/api/course',
                method: 'POST',

                data: {
                    access_token: token,
                    name: courseName,
                    crn: courseCRN
                },
                success: function() {
                    document.location.href = "/dashboard";
                }
            })
        }

    });
});

function identifierFormatter(value, row, index) {
    return [
        '<a href="#" >',
        value,
        '</a>'].join('');
}