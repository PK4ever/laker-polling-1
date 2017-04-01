<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Course Page</title>
    <asset:stylesheet href="bootstrap.min.css"/>
    <asset:stylesheet href="bootstrap-theme.min.css"/>
    <!-- jQuery (necessary for Bootstrap"s JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>
<body>
    <div class="navbar navbar-default" role="navigation">
        <div class="navbar-header">
            <asset:image class="img-responsive navbar-brand" src="logo.png"/>
            <a class="navbar-brand">Course Page</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <button onclick="logout()" class="btn btn-default navbar-right navbar-btn">Logout</button>
            </ul>
        </div>
    </div>
    <a href="/dashboard">Return to Dashboard</a>
    <h1 id="coursePageTitle"></h1> <!-- Class name here -->
    <div class="form-group" style="text-align: center;">
        <button type="submit" class="btn btn-success" id="createPollButton" action="#">Create Polling Session</button>
    </div>
    <div class="form-group" style="text-align: center;">
        <a href="/course/roster" class="btn btn-success" role="button">Roster</a>
    </div>

<asset:javascript src="jquery-3.2.0.min.js"/>
<script src="https://apis.google.com/js/platform.js"></script>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.css">

<!-- Latest compiled and minified JavaScript -->
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>

<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
<asset:javascript src="instructor.js"/>
<script>
window.onload=prepareClassTitle(${session.courseId});
</script>
</body>
</html>