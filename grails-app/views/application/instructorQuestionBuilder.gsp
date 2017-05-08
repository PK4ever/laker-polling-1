<!DOCTYPE html>
<html lang="en">
<head>
    <asset:stylesheet href="bootstrap.min.css"/>
    <asset:stylesheet href="style.css"/>
    <asset:stylesheet href="agency.min.css"/>
    <asset:stylesheet href="agency.css"/>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Create a Question</title>
    <!-- jQuery (necessary for Bootstrap"s JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>
<body>
    <nav id="mainNav" class="navbar navbar-default navbar-custom navbar-fixed-top">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header page-scroll">
            <button type="button" onclick="logout()" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span> LOGOUT <i class="fa fa-bars"></i>
            </button>
            <a class="navbar-brand page-scroll" href="/dashboard">

                <asset:image src="logo2.png" class="logo"/>

                %{--<img src="logo.png" style="height: 60px !important; width: 120px !important; position: absolute; top: 0%">--}%
            </a>
            <a id="coursePageTitle" class="navbar-brand" style="position: absolute; left: 45%; font-size: x-large"></a>
    

        <!--<a class="navbar-brand page-scroll" href="#page-top">LOGO HERE</a>-->
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    %{--<button onclick="logout()" class="btn btn-default navbar-right navbar-btn">Logout</button>--}%
                    <a class="logout" onclick="logout()" >LogOut</a>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container-fluid -->
</nav>

<section>

<a href="/course?courseId=${session.courseId}" style="margin-left: 60px; href="/course?courseId=${session.courseId}" class="btn btn-default btn-md">
          <span class="glyphicon glyphicon-arrow-left"></span> Back to Course
</a>

<div class="form-group" style="text-align: center;">
    <form id="question-form" method="post">
        <div class="btn-group" data-toggle="buttons" id="checkboxDiv">
            <label class="btn btn-default" id="answers">
            <input type="checkbox" autocomplete="off" name="vehicle" id="checkbox1">A</label><br>
            <label class="btn btn-default" id="answers">
            <input type="checkbox" autocomplete="off" name="vehicle" id="checkbox2">B</label><br>
            <label class="btn btn-default" id="answers">
            <input type="checkbox" autocomplete="off" name="vehicle" id="checkbox3">C</label><br>
            <label class="btn btn-default" id="answers">
            <input type="checkbox" autocomplete="off" name="vehicle" id="checkbox4">D</label><br>
            <label class="btn btn-default" id="answers">
            <input type="checkbox" autocomplete="off" name="vehicle" id="checkbox5">E</label><br>
        </div>
        <br>
        <br>
        <input class="btn btn-success" type="button" data-course-id="${session.courseId}" id="submit-question-btn" value="Submit">
        <input class="btn btn-success" type="button" data-course-id="${session.courseId}" id="submit-question-btn2" value="Submit another question" style="display:none;">
    </form>
        <button class="btn btn-danger" id="close-question-btn" data-course-id="${session.courseId}" style="display: none; width: 200px; text-align: center;" >Close Question</button>
        <br> <br>
        <div id="resultLink">
            
        </div>
</div>
</section>

<script src="https://apis.google.com/js/platform.js"></script>

<asset:javascript src="main.js"/>
<asset:javascript src="instructor.js"/>
<asset:javascript src="question.js"/>
<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
<script>
    window.onload= function(){
        $.ajax({
            url: '/user/auth',
            method: 'GET',
            success: function(data){
                var token = data.data.token
                $.ajax({
                    url:'/api/question/active?access_token=' + token + '&course_id=' + ${session.courseId},
                    method: 'GET',
                    success: function(data){
                        question_id = data.questionId
                        prepareClassTitle(${session.courseId})
                        toggleButtons()
                    },
                    error: function(){
                        prepareClassTitle(${session.courseId})
                    }
                });
            }
        });
    }
</script>

</body>
</html>