<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Course Page</title>
    <asset:stylesheet href="bootstrap.min.css"/>
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
<div class="navbar navbar-default" role="navigation">
    <div class="navbar-header">
        <asset:image class="img-responsive navbar-brand" src="logo.png"/>
        <a class="navbar-brand">Poll</a>
    </div>
    <div class="navbar-collapse collapse">
        <ul class="nav navbar-nav">
            <a onclick="logout()" class="btn btn-default navbar-right navbar-btn logout">Logout</a>
        </ul>
    </div>
</div>

<a href="/dashboard" style="margin-left: 60px; href="/dashboard" class="btn btn-default btn-md">
          <span class="glyphicon glyphicon-arrow-left"></span> Back to Dashboard
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
        <button class="btn" id="submitAnswerLive" data-course-id="${session.courseId}">Submit answer</button>
        </div>
    </form>
</div>
<div class="form-group" style="text-align: center;">
    <a href="/course/viewresults?courseId=${session.courseId}" class="btn btn-info" role="button">View Results</a>
</div>

<script src="https://apis.google.com/js/platform.js"></script>
<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
<asset:javascript src="question.js"/>
</body>
</html>
