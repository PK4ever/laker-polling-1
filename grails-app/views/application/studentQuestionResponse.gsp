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
<div class="form-group" style="text-align: right;">
    <form action="/action_page.php" method="get">
      <input type="checkbox" name="A" value="A">A<br>
      <input type="checkbox" name="B" value="B">B<br>
      <input type="checkbox" name="C" value="C">C<br>
      <input type="checkbox" name="D" value="D">D<br>
      <input type="checkbox" name="E" value="E">E<br>
      <input type="submit" name="Submit" value="Submit">Submit Poll<br>
    </form>
</div>
<div class="form-group" style="text-align: right;">
    <a href="#" class="btn btn-success" role="button">View Results</a>
</div>
<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
</body>
</html>
