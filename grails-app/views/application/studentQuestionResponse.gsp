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
<a href="/course?courseId=${session.courseId}">< Return to Course Page</a>
<div class="form-group" style="text-align: center;">
    <form action="/action_page.php" method="get">
        <div class="btn-group" data-toggle="buttons" >
            <label class="btn btn-default" id="answers">
                <input type="checkbox" autocomplete="off" name="vehicle" value="A">A</label><br>
            <label class="btn btn-default" id="answers">
                <input type="checkbox" autocomplete="off" name="vehicle" value="B">B</label><br>
            <label class="btn btn-default" id="answers">
                <input type="checkbox" autocomplete="off" name="vehicle" value="C">C</label><br>
            <label class="btn btn-default" id="answers">
                <input type="checkbox" autocomplete="off" name="vehicle" value="D">D</label><br>
            <label class="btn btn-default" id="answers">
                <input type="checkbox" autocomplete="off" name="vehicle" value="E">E</label><br>
        </div>
        <br>
        <input class="btn btn-success" type="submit" id="submit-btn" value="Submit Answer">
    </form>
</div>
<div class="form-group" style="text-align: center;">
    <a href="#" class="btn btn-success" role="button">View Results</a>
</div>

    <!-- user interaction -->
    <script>
        $('.checkbox').change(function() {
            $(this).toggleClass("answer-selected");
        })
    </script>

<script src="https://apis.google.com/js/platform.js"></script>
<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
</body>
</html>
