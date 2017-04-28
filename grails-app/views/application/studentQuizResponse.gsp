<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Course Page</title>
    <asset:stylesheet href="bootstrap.min.css"/>
    <asset:stylesheet href="agency.min.css"/>
    <asset:stylesheet href="agency.css"/>
    <asset:stylesheet href="style.css"/>
    <!-- jQuery (necessary for Bootstrap"s JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

    <style>
        .answer-selected {
            background-color: green;
        }
    </style>
</head>
<body>
<<nav id="mainNav" class="navbar navbar-default navbar-custom navbar-fixed-top">
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

    <a href="/dashboard" style="margin-left: 60px;" href="/dashboard" class="btn btn-default btn-md">
              <span class="glyphicon glyphicon-arrow-left"></span> Back to Dashboard
    </a>

    <div class="form-group" style="text-align: center;">
        <h1 id="question-text"></h1>
        <h2 id="answer0"></h2>
        <h2 id="answer1"></h2>
        <h2 id="answer2"></h2>
        <h2 id="answer3"></h2>
        <h2 id="answer4"></h2>
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
            </div>
        </form>
        <button class="btn btn-success" id="submitAnswer" data-course-id="${session.courseId}">Submit answer</button>
    </div>
</section>
<script src="https://apis.google.com/js/platform.js"></script>
<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
<asset:javascript src="question.js"/>
<asset:javascript src="studentQuizResponse.js"/>

<script>
    window.onload=prepareClassTitle(${session.courseId});
    window.onload=prepareQuizTitle(${session.quizId});
    window.onload=prepareQuestionIndex(${session.questionIndex});
</script>
</body>
</html>
