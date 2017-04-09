<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Instructor</title>
    <asset:stylesheet href="bootstrap.min.css"/>
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
            <a class="navbar-brand page-scroll" href="/dashboard">
                <asset:image src="logo2.png"
                             style="height: 60px !important; width: 120px !important; position: absolute; top: 0%"/>
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
                    <a onclick="logout()" >LogOut</a>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container-fluid -->
</nav>
<section>
<div class="container">
    <a href="/course?courseId=${session.courseId}">Back to Course Page</a>
    <div class="row">
        <div class="col-sm-3"></div>
        <div class="col-sm-6">
        <form>
            Date:
            <input id="datepicker" type="date" name="date" onchange="changeDate(this)" />
        </form>
        </div>
        <div class="col-sm-3"></div>
    </div><br>  

    <div class="row">
        <div class="col-sm-3"></div>
        <div class="col-sm-6">
            <div id="attendance" class="table-responsive">
                <table id="attendanceTable" class="table">
                    <thead>
                    <tr>
                        <th class="col-md-1" data-field="email">Email</th>
                        <th class="col-md-1" data-field="attended">Attended</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
        <div class="col-sm-3"></div>
    </div>
</section>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.css">

<asset:javascript src="jquery-3.2.0.min.js"/>
<script src="https://apis.google.com/js/platform.js"></script>

<!-- Latest compiled and minified JavaScript -->
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>

<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
<asset:javascript src="instructor.js"/>
<asset:stylesheet href="bootstrap.css"/>
<asset:stylesheet href="agency.min.css"/>
<asset:stylesheet href="agency.css"/>
<asset:stylesheet href="style.css"/>

<script>
    window.onload=prepareClassTitle(${session.courseId});
</script>

</body>
</html>