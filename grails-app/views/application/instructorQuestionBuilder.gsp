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
    <nav id="mainNav" class="navbar navbar-default navbar-custom navbar-fixed-top">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header page-scroll">
                <button type="button" onclick="logout()" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span> LOGOUT <i class="fa fa-bars"></i>
                </button>
                <a class="navbar-brand page-scroll" href="#page-top">
                    <asset:image src="logo.png"
                                 style="height: 60px !important; width: 120px !important; position: absolute; top: 0%"/>
                </a>
                <a id="pageName" class="navbar-brand" style="position: absolute; left: 45%; font-size: x-large">Instructor's Dashboard</a>

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
        </div>
    </nav>
<div class="navbar navbar-default" role="navigation">
    <div class="navbar-header">
        <asset:image class="img-responsive navbar-brand" src="logo.png"/>
        <a class="navbar-brand">Make a Question</a>
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
        <input class="btn btn-success" type="submit" id="submit-btn" value="Submit">
    </form>
</div>
<asset:javascript src="auth/instructor.js"/>
<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
</body>
</html>