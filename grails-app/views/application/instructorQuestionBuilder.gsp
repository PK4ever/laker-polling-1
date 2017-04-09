<!DOCTYPE html>
<html lang="en">
<head>
    <asset:stylesheet href="bootstrap.min.css"/>
    <asset:stylesheet href="style.css"/>
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
                <asset:image src="logo.png"
                             style="height: 60px !important; width: 120px !important; position: absolute; top: 0%"/>
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
                    <a onclick="logout()" >LogOut</a>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container-fluid -->
</nav>

<section>

<a href="/course?courseId=${session.courseId}" style="margin-left: 60px; href="/course?courseId=${session.courseId}" class="btn btn-default btn-md">
          <span class="glyphicon glyphicon-arrow-left"></span> Back to Course Page
</a>

<div class="form-group" style="text-align: center;">
    <form id="question-form" method="post">
        <div class="btn-group" data-toggle="buttons" >
            <label class="btn btn-default" id="answers">
            <input type="checkbox" autocomplete="off" name="vehicle">A</label><br>
            <label class="btn btn-default" id="answers">
            <input type="checkbox" autocomplete="off" name="vehicle" >B</label><br>
            <label class="btn btn-default" id="answers">
            <input type="checkbox" autocomplete="off" name="vehicle" >C</label><br>
            <label class="btn btn-default" id="answers">
            <input type="checkbox" autocomplete="off" name="vehicle">D</label><br>
            <label class="btn btn-default" id="answers">
            <input type="checkbox" autocomplete="off" name="vehicle" >E</label><br>
        </div>
        <br>
        <br>
        <input class="btn btn-success" type="button" data-course-id="${session.courseId}" id="submit-question-btn" value="Submit">
    </form>
        <button class="btn btn-danger" id="close-question-btn" style="display: none; width: 200px; text-align: center;" >Close Question</button>
        <br> <br>
        <a href="course/viewresults?courseId=${session.courseId}" class="btn btn-default" id="show-results-btn">Show Results</a>
</div>
</section>

<asset:javascript src="jquery-3.2.0.min.js"/>
<script src="https://apis.google.com/js/platform.js"></script>


<asset:javascript src="instructor.js"/>
<asset:javascript src="question.js"/>
<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
<asset:stylesheet href="agency.min.css"/>
<asset:stylesheet href="agency.css"/>
<script>
    window.onload=prepareClassTitle(${session.courseId});
</script>

</body>
</html>