var courses = [];
var token = '';
var courseID;

$(function() {
    $.ajax({
        url: '/user/auth',
        method: "GET",

        success: function(data){
            token = data.data.token;
            var type = data.data.user.type;
            //GET COURSES AND DISPLAY ON THE PAGE
            var urlstr = '/api/course?=' + token;
            $.ajax({
                url: '/api/course',
                method: "GET",
                data: {
                    access_token: token
                },


                success: function(data){
                    courses = data.data.courses;
                    var i;
                    var courseDiv = document.getElementById("courses");
                    if (courses.length == 0) {
                        var string = "<p style=\"text-align: center;\">You are not enrolled in any courses.</p>";
                        var div = document.createElement("div")
                        div.innerHTML = string;
                        courseDiv.appendChild(div);
                    }
                    for (i = 0; i < courses.length; i++) {
                        var string = courseHTML(courses[i].name,courses[i].crn, courses[i].id)
                        var div = document.createElement("div")
                        div.innerHTML = string;
                        courseDiv.appendChild(div);
                    }

                }

            });

            //Check role
            $.ajax({
                url: '/api/user/role?access_token='+token,
                type: 'GET',
                success: function(data) {
                    var availableRoles = data.data.role.available;
                    var isInstructor = false;
                    type;
                    $.each(availableRoles, function(i,obj) {
                      if (obj === 'INSTRUCTOR') { isInstructor = true; return false;}
                    });
                    if(isInstructor)
                        document.getElementById('roleButtonDiv').style.visibility="visible";
                    else  
                        document.getElementById('roleButtonDiv').style.visibility="hidden";
                },
                error: function(jqXHR, textStatus, errorMessage) {
                    console.log(errorMessage)
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
            Name = user.email;

            profpic = user.imageUrl;
            var courseDiv = document.getElementById("userName");

            var string = '<h3 class="section-heading">Hello, you are currently logged in as '+Name+'.</h3>';
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

$('#roleButton').on('click', function(event) {
    $.ajax({
        url: '/user/auth',
        type: 'GET',
        success: function(data) {
            var token = data.data.token;
            var userId = data.data.user.id;
            $.ajax({
                url: '/api/user/role?access_token='+token+'&user_id='+ userId +'&current=INSTRUCTOR',
                type: 'PUT',
                success: function(data) {
                    window.location.href = "/dashboard";
                },
                error: function(jqXHR, textStatus, errorMessage) {
                    console.log(errorMessage)
                }

            });
        }
    });
});


function courseHTML(courseName, crn, id) {
    //var str = '<div class="col-md-4 col-sm-6 portfolio-item" style="box-shadow: 0px 0px 0px gray; padding: 20px;">'
    var str = '<div class="col-md-4 col-sm-6 portfolio-item" style="box-shadow: 10px 10px 50px gray; padding: 10px;">'
    //var str = '<div class="col-md-4 col-sm-6 portfolio-item" style="box-shadow: 0px 0px 0px gray; padding: 10px;">'
    str += '<a href="/course?courseId=' + id + '" class="portfolio-link" data-toggle="modal">'
    str += '<a href="/course?courseId=' + id + '" class="portfolio-link" data-toggle="modal">'
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

function checkquestionfromcourse(crn) {
    //retrieve the number of question of course
}

function prepareClassTitle(cId) {
    courseId = cId;
}