<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Instructor</title>
    <asset:stylesheet href="bootstrap.min.css"/>
    <asset:stylesheet href="bootstrap.css"/>
    <asset:stylesheet href="agency.min.css"/>
    <asset:stylesheet href="agency.css"/>
    <asset:stylesheet href="style.css"/>
    <asset:stylesheet href="jquery-clockpicker.min.css"/>
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

                <asset:image src="logo2.png" class="logo"/>

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
                    <a class="logout" onclick="logout()" >LogOut</a>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container-fluid -->
</nav>
<section>
<div class="container">
<a href="/course?courseId=${session.courseId}" style="margin-left: 60px; href="/course?courseId=${session.courseId}" class="btn btn-default btn-md">
          <span class="glyphicon glyphicon-arrow-left"></span> Return to Course
</a>
    <div class="row">
        <div class="col-sm-3"></div>
        <div class="col-sm-6">
            <div id="quizzes" class="table-responsive">
                <table id="quizTable" class="table">
                    <thead>
                    <tr>
                        <th class="col-md-1" data-field="id">ID</th>
                        <th class="col-md-1" data-field="name" data-formatter="identifierFormatter">Quiz Name</th>
                        <th class="col-md-1" data-field="startDate" data-formatter="dateFormatter">Start Time</th>
                        <th class="col-md-1" data-field="endDate" data-formatter="dateFormatter">End Time</th>
                        <th class="col-md-1" data-field="id" id="delete-quiz-btn" data-formatter="deleteQuizFormatter">Delete</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
        <div class="col-sm-3"></div>
    </div>

    <div class="row">
        <div class="col-sm-3"></div>
        <div class="col-sm-6">
        <form id="quiz-form" method="post">
            <!-- TODO: Make the date pickers allow for time selection -->


            <div class="form-group">
                <label class="control-label" for="startDate">Start Date</label>
                <input class="form-control" id="startDate" name="startDate" placeholder="YYYY-MM-DD" type="text"  readonly="readonly"/>
            </div>

            <div class="form-group">
                <label class="control-label" for="startTime">Start Time</label>
                
                <div class="input-group clockpicker">
                    <input id="startTime" type="text" class="form-control" value="09:30" readonly="readonly">
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-time"></span>
                    </span>
                </div>
            </div>

            <div class="form-group"> 
                <label class="control-label" for="endDate">End Date</label>
                <input class="form-control" id="endDate" name="endDate" type="text" placeholder="YYYY-MM-DD" readonly="readonly"/>
            </div>

            <div class="form-group">
                <label class="control-label" for="endTime">End Time</label>

                <div class="input-group clockpicker">
                    <input id="endTime" type="text" class="form-control" value="09:30" readonly="readonly">
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-time"></span>
                    </span>
                </div>
            </div>

            Quiz Name:
            <input type="text" name="quizName" id="quizName"><br><br>
            <input class="btn btn-success" type="button" data-course-id="${session.courseId}" id="newQuizButton" value="Create New Quiz">
        </form>
        </div>
        <div class="col-sm-3"></div>
    </div><br>

    <div id="deleteQuizModal" class="modal fade" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content clean-container">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title heading">Delete Quiz?</h4>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to delete this quiz?</p>
                </div>
                <div class="modal-footer">
                    <button id="confirmDeleteQuiz" type="button" class="btn btn-danger btn-ok js-deleteQuiz" data-dismiss="modal" data-quiz-id="">Yes</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                </div>
            </div>
        </div>
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
<asset:javascript src="main.js"/>
<asset:javascript src="instructor.js"/>
<asset:javascript src="atHome.js"/>

<asset:javascript src="jquery-clockpicker.min.js"/>


<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/js/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-timepicker/0.5.2/js/bootstrap-timepicker.min.js"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.1/moment.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment-timezone/0.5.13/moment-timezone.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment-timezone/0.5.13/moment-timezone-with-data-2012-2022.min.js"></script>



<script>
    window.onload=prepareClassTitle(${session.courseId});
</script>

<script type="text/javascript">
    $('.clockpicker').clockpicker({
        autoclose: true
    });
</script>

<script>
    var start_date_input;
    var end_date_input;
    $(document).ready(function() {
      start_date_input = $('input[name="startDate"]');
      end_date_input = $('input[name="endDate"]');
      var options = {
        format: 'yyyy-mm-dd',
        todayHighlight: true,
        autoclose: true,
        disableTouchKeyboard: true
      };
      start_date_input.datepicker(options);
      end_date_input.datepicker(options);
    });
</script>

</body>
</html>