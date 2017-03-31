<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Course Page</title>
    <asset:stylesheet href="bootstrap.min.css"/>
    <asset:stylesheet href="bootstrap-theme.min.css"/>
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
    <a href="#">Return to Dashboard</a>
    <h1></h1> <!-- Class name here -->
    <div class="form-group" style="text-align: center;">
        <button type="submit" class="btn btn-success" id="createPollButton" action="#">Create Polling Session</button>
    </div>
    <div class="form-group" style="text-align: center;">
        <button type="submit" class="btn btn-success" id="studentListButton" action="#">Student List</button>
    </div>
<!-- Include asset ref for js file, eventually! -->
</body>
</html>