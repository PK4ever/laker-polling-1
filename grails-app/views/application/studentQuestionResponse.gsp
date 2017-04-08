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

    

    <style> 
        .answer-selected {
            color: red;
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
            <button onclick="logout()" class="btn btn-default navbar-right navbar-btn">Logout</button>
        </ul>
    </div>
</div>

<a href="/dashboard" style="margin-left: 60px; href="/dashboard" class="btn btn-default btn-md">
          <span class="glyphicon glyphicon-arrow-left"></span> Back to Dashboard
</a>

<div class="container">
  <div class="col-sm-4"></div>
  <div class="col-sm-4">
  <div class="form-group">
    <form method="get">
        <div class="checkbox">
            <label><input type="checkbox" value="A">A</label>
        </div>
        <div class="checkbox">
          <label><input type="checkbox" value="B">B</label>
        </div>
        <div class="checkbox">
            <label><input type="checkbox" value="C">C</label>
        </div>
        <div class="checkbox">
          <label><input type="checkbox" value="D">D</label>
        </div>
        <div class="checkbox">
          <label><input type="checkbox" value="E">E</label>
        </div>

        <input type="submit" class="btn btn-success" name="Submit" value="Submit answer"><br>

    </form>
  </div>
    <div class="form-group">
        <a href="#" class="btn btn-info" role="button">View Results</a>
    </div>
    </div>
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
