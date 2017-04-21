<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Course Page</title>
    <asset:stylesheet href="bootstrap.min.css"/>
    <!-- jQuery (necessary for Bootstrap"s JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

	<asset:javascript src="amcharts_3.21.1.free/amcharts/amcharts.js"/>
	<asset:javascript src="amcharts_3.21.1.free/amcharts/serial.js"/>
	<script type="text/javascript" src="https://www.amcharts.com/lib/3/themes/black.js"></script>
<asset:javascript src="auth/resultData.js"/>
   
</head>
<body class="bg-light-gray">
<div class="navbar navbar-default" role="navigation">
    <div class="navbar-header">
        <asset:image src="logo2.png" class="logo"/>
        <a class="navbar-brand">Poll</a>
    </div>
    <div class="navbar-collapse collapse">
        <ul class="nav navbar-nav">
            <a onclick="logout()" class="btn btn-default navbar-right navbar-btn logout">Logout</a>
        </ul>
    </div>
</div>
<a href="/course/createquestion?courseId=${session.courseId}" style="margin-left: 60px; href="/course/createquestion?courseId=${session.courseId}" class="btn btn-default btn-md">
          <span class="glyphicon glyphicon-arrow-left"></span> Back to Question Page
</a>
		
	
<section>
<div class="container">
		<div id="chartdiv" style="width: 100%; height: 400px; background-color: #D4D4D4;" ></div>
</div>
</section>


<script src="https://apis.google.com/js/platform.js"></script>
<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
<asset:javascript src="instructor.js"/>
<asset:javascript src="question.js"/>



<script>
	 window.onload=prepareQuestionId(${session.courseId}, ${session.questionId});
</script>
</body>
</html>