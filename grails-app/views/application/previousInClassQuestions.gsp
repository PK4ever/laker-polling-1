<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <asset:stylesheet href="bootstrap.min.css"/>
    <asset:stylesheet href="bootstrap.css"/>
    <asset:stylesheet href="agency.min.css"/>
    <asset:stylesheet href="agency.css"/>
    <asset:stylesheet href="style.css"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>


    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/js/bootstrap-datepicker.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/css/bootstrap-datepicker3.css"/>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

<title>Closed Questions</title>
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
<div class="container-fluid">
     <div class="col-md-2">
         <a href="/course?courseId=${session.courseId}" style="margin-left: 60px;" href="/course?courseId=${session.courseId}" class="btn btn-default btn-md">
             <span class="glyphicon glyphicon-arrow-left"></span> Return to Course
         </a>
     </div>
     <div class="row">
         <div class="col-md-2 col-sm-4 col-xs-12">
              <form> 
                  <div class="form-group"> <!-- Date input -->
                      <label class="control-label" for="date">Date</label>
                      <input class="form-control" id="date" name="date" placeholder="YYYY-MM-DD" type="text" readonly="readonly"/>
                  </div>
              </form>
         </div>
     </div>
</div>
<div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <div id="questionPerformanceContainer" class="table-responsive">
            </div>
        </div>
    </div>
</section>

<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.css">

<script src="https://apis.google.com/js/platform.js"></script>

<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>

<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
<asset:javascript src="main.js"/>
<asset:javascript src="instructor.js"/>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

<script>
    window.onload = prepareClassTitle(${session.courseId});
</script>

<script>
    var date_input;
    $(document).ready(function() {
      date_input = $('input[name="date"]');
      var options = {
        format: 'yyyy-mm-dd',
        todayHighlight: true,
        autoclose: true,
        clearBtn: true,
        orientation: 'auto top'
      };
      // date_input.datepicker(options);
      date_input.datepicker(options).on('changeDate', function(event) {
        console.log('date changed')

        getQuestionsFor($(this).val(), ${session.courseId});
      });
    });
</script>

</body>
</html>