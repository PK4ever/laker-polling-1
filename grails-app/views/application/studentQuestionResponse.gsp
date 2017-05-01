<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Course Page</title>
   
    <!-- jQuery (necessary for Bootstrap"s JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
     <asset:stylesheet href="bootstrap.min.css"/>
    <asset:stylesheet href="style.css"/>
    <asset:stylesheet href="agency.min.css"/>
    <asset:stylesheet href="agency.css"/>
    <style>
        .answer-selected {
            background-color: green;
        }
    </style>
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
    <a href="/dashboard" style="margin-left: 60px; href="/dashboard" class="btn btn-default btn-md">
    <span class="glyphicon glyphicon-arrow-left"></span> Return to Course
</a>
<div class="form-group" style="text-align: center;">
    <form id="answer-form" method="post">
        <div class="btn-group" data-toggle="buttons" >
            <label class="btn btn-default answer-btn" id="answers">
                <input type="checkbox" autocomplete="off">A</label><br>
            <label class="btn btn-default answer-btn" id="answers">
                <input type="checkbox" autocomplete="off">B</label><br>
            <label class="btn btn-default answer-btn" id="answers">
                <input type="checkbox" autocomplete="off">C</label><br>
            <label class="btn btn-default answer-btn" id="answers">
                <input type="checkbox" autocomplete="off">D</label><br>
            <label class="btn btn-default answer-btn" id="answers">
                <input type="checkbox" autocomplete="off">E</label><br>
        </div>
        <div class="form-group">
        <br>
        <input class="btn btn-success" type="button" id="submitAnswerLive" data-course-id="${session.courseId}" value="Submit answer">
        </div>
    </form>
</div>
    </section>
<!--<div class="form-group" style="text-align: center;">
    <a href="/course/viewresults?courseId=${session.courseId}" class="btn btn-info" role="button">View Results</a>
</div>-->

<script src="https://apis.google.com/js/platform.js"></script>
<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
<asset:javascript src="question.js"/>
</body>
</html>
