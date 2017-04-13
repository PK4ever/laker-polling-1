<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Instructor</title>
    <asset:stylesheet href="bootstrap.min.css"/>
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

                <asset:image src="logo2.png"
                             style="height: 80px !important; width: 120px !important; position: absolute; top: 0%"/>

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
<div class="container">
<a href="/course?courseId=${session.courseId}" style="margin-left: 60px; href="/course?courseId=${session.courseId}" class="btn btn-default btn-md">
          <span class="glyphicon glyphicon-arrow-left"></span> Back to Course Page
</a>
    <div class="row">
        <div class="col-sm-3"></div>
        <div class="col-sm-6">
            <div id="quizzes" class="table-responsive">
                <table id="quizTable" class="table">
                    <thead>
                    <tr>
                        <th class="col-md-1" data-field="name">Quiz Name</th>
                        <th class="col-md-1" data-field="startTime">Start Time</th>
                        <th class="col-md-1" data-field="endTime">End Time</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
        <div class="col-sm-3"></div>
    </div>

    <div class="row">
        <div class="col-sm-3"></div>
        <div class="col-sm-6">
        <form id="quiz-form" method="post">
            <!-- TODO: Make the date pickers allow for time selection -->
            Start Time:
            <input id="startDate" type="date" name="startdate" onchange="changeDate(this)" /> <br><br>
            End Time:
            <input id="endDate" type="date" name="enddate" onchange="changeDate(this)" /> <br><br>
            Quiz Name:
            <input type="text" name="quizName"><br><br>
            <input class="btn btn-success" type="button" data-course-id="${session.courseId}" id="newQuizButton" value="Create New Quiz">
        </form>
        </div>
        <div class="col-sm-3"></div>
    </div><br>
</section>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.css">

<asset:javascript src="jquery-3.2.0.min.js"/>
<script src="https://apis.google.com/js/platform.js"></script>

<!-- Latest compiled and minified JavaScript -->
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>

<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
<asset:javascript src="instructor.js"/>
<asset:javascript src="atHome.js"/>
<asset:stylesheet href="bootstrap.css"/>
<asset:stylesheet href="agency.min.css"/>
<asset:stylesheet href="agency.css"/>
<asset:stylesheet href="style.css"/>

<script>
    window.onload=prepareClassTitle(${session.courseId});
</script>

</body>
</html>