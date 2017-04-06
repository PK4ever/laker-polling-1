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
        <a class="navbar-brand">Poll</a>
    </div>
    <div class="navbar-collapse collapse">
        <ul class="nav navbar-nav">
            <button onclick="logout()" class="btn btn-default navbar-right navbar-btn">Logout</button>
        </ul>
    </div>
</div>
<a href="#">< Return to Course Page</a>
<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
</body>
</html>