<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <asset:stylesheet href="bootstrap.css"/>
    <asset:stylesheet href="agency.min.css"/>
    <asset:stylesheet href="agency.css"/>
    <asset:stylesheet href="style.css"/>
    <title>Student</title>
</head>

<body class="bg-light-gray">
<nav id="mainNav" class="navbar navbar-default navbar-custom navbar-fixed-top">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header page-scroll">
            <button type="button" onclick="logout()" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span> LOGOUT <i class="fa fa-bars"></i>
            </button>
            <a class="navbar-brand page-scroll" href="#page-top">
                <asset:image src="logo2.png"
                             style="height: 60px !important; width: 120px !important; position: absolute; top: 0%"/>
                %{--<img src="logo.png" style="height: 60px !important; width: 120px !important; position: absolute; top: 0%">--}%
            </a>
            <a id="pageName" class="navbar-brand" style="position: absolute; left: 45%; font-size: x-large">Student Dashboard</a>
        %{--<a id="pageName" class="page-scroll navbar-brand" style="position: absolute; left: 40%; font-size: xx-large">Student Dashboard</a>--}%

        <!--<a class="navbar-brand page-scroll" href="#page-top">LOGO HERE</a>-->
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    %{--<button onclick="logout()" class="btn btn-default navbar-right navbar-btn">Logout</button>--}%
                    <a style="cursor: pointer" onclick="logout()" >LogOut</a>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container-fluid -->
</nav>

<section id="portfolio">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                %{--<div id="profilePic"></div>--}%
                %{--<asset:image class="img-circle" style="width: 8%" src="logo.png"/>--}%
                %{--<img src="https://lh3.googleusercontent.com/-OGPqbf7NrUw/AAAAAAAAAAI/AAAAAAAAADU/UqOf3-ywmI4/s96-c/photo.jpg" class="img-circle" style="width: 8%">--}%
                %{--<h2 class="section-heading">Hello, Paul Kwoyelo</h2>--}%
                <div id="userName"></div>
                <div id="profilePic"></div>
                <h3 class="section-subheading text-muted">Welcome to your dashboard</h3>

            </div>
        </div>

        <h3 class="text-muted">Your Courses:</h3>
        <div class="row" id="courses">
            %{--courses are displayed here--}%
            </div>

        </div>
    </div>
</section>



<asset:javascript src="jquery-3.2.0.min.js"/>
<script src="https://apis.google.com/js/platform.js"></script>

<!-- Latest compiled and minified CSS -->
%{--<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.css">--}%

<!-- Latest compiled and minified JavaScript -->
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>

<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
<asset:javascript src="bootstrap.min.js"/>
<asset:javascript src="student.js"/>


</body>
</html>