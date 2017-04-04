var courses = [];
var token = '';

$(function() {
    $.ajax({
	    url: '/user/auth',
	    method: "GET",

	    success: function(data){
	    	token = data.data.token;
			//GET COURSES AND DISPLAY ON THE PAGE
			var urlstr = '/api/course?=' + token;
		    $.ajax({
			    url: '/api/course',
			    method: "GET",
			    data: {
			    	access_token: token
			    },


			    success: function(data){

			    	//courses = data.data.courses;
					courses = [
						{"name":"csc 212","crn":1123123,"students":8,"id":1},
						{"name":"csc 212","crn":1123123, "students":8,"id":1},
						{"name":"csc 212","crn":1123123,"students":8,"id":1},
						{"name":"csc 212","crn":1123123,"students":8,"id":1},
						{"name":"csc 212","crn":1123123,"students":8,"id":1},
						//{"name":"csc 212","crn":1123123,"students":8,"id":1},
						//{"name":"csc 212","crn":1123123,"students":8,"id":1},
						//{"name":"csc 212","crn":1123123,"students":8,"id":1},
						//{"name":"csc 212","crn":1123123,"students":8,"id":1},
						//{"name":"csc 212","crn":1123123,"students":8,"id":1},
						//{"name":"csc 212","crn":1123123,"students":8,"id":1},
						//{"name":"csc 212","crn":1123123,"students":8,"id":1},
						//{"name":"csc 212","crn":1123123,"students":8,"id":1},
						{"name":"csc 212","crn":1123123,"students":8,"id":1}]
				    //$('#courseTable').bootstrapTable({
				    //    data: courses
				    //});


					var i;
					var courseDiv = document.getElementById("courses");
					for (i = 0; i < courses.length; i++) {
						var string = courseHTML(courses[i].name,courses[i].crn)
						var div = document.createElement("div")
							div.innerHTML = string;
						courseDiv.appendChild(div);
					}

				}

			});

		}
	});
	//GET USER INFO AND DISPLAY ON THE PAGE
	var Name = '';
	var profpic = '';
	$.ajax({
		url: '/user/auth',
		method: "GET",

		success: function(data){
			var user = data.data.user
			Name = user.name;

			profpic = user.imageUrl;
			var courseDiv = document.getElementById("userName");

			var string = '<h2 class="section-heading">Hello, '+Name+'</h2>';
			var div = document.createElement("div")
			div.innerHTML = string;
			courseDiv.appendChild(div);

			var pic = document.getElementById("profilePic");
			var picString ='<img src="'+profpic+'" class="img-circle" style="width: 5%">';

			var profDiv = document.createElement("div");
			profDiv.innerHTML = picString;
			pic.appendChild(profDiv);


		}
	});
});



function courseHTML(courseName, crn) {
	//var str = '<div class="col-md-4 col-sm-6 portfolio-item" style="box-shadow: 0px 0px 0px gray; padding: 20px;">'
	var str = '<div class="col-md-4 col-sm-6 portfolio-item" style="box-shadow: 10px 10px 50px gray; padding: 10px;">'
	//var str = '<div class="col-md-4 col-sm-6 portfolio-item" style="box-shadow: 0px 0px 0px gray; padding: 10px;">'
	str += '<a href="#portfolioModal2" class="portfolio-link" data-toggle="modal">'
		str += '<div class="portfolio-hover">'
		str += '<div class="portfolio-hover-content">'
		str += '<i class="fa fa-plus fa-3x"></i></div></div>'
		//str += '<asset:image class="img-responsive" src="logo.png" alt=""/>'
		//str += '<img src="assets/images/startup-framework.png" class="img-responsive" alt=""></a>'
	str += '<div class="portfolio-caption"><h4>' +courseName +'</h4><p class="text-muted"> CRN: '+ crn + '</p></div></div>'
	return str
};



function identifierFormatter(value, row, index) {
    return [
            '<a class="like" href="javascript:void(0)" title="Like">',
                value,
            '</a>'].join('');
}