<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Create Quiz</title>
    <asset:stylesheet href="bootstrap.min.css"/>
    <asset:stylesheet href="bootstrap.css"/>
    <asset:stylesheet href="agency.min.css"/>
    <asset:stylesheet href="agency.css"/>
    <asset:stylesheet href="style.css"/>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.css">
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
             style="height: 80px !important; width: 120px !important; position: absolute; top: 0%"/>

</a>
<a id="coursePageTitle" class="navbar-brand" style="position: absolute; left: 45%; font-size: x-large"></a>


<!--<a class="navbar-brand page-scroll" href="#page-top">LOGO HERE</a>-->
</div>

<!-- Collect the nav links, forms, and other content for toggling -->
<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
    <ul class="nav navbar-nav navbar-right">
        <li>
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
        <span class="glyphicon glyphicon-arrow-left"></span> Back to Course Page
    </a>

        <div class="row">
            <div class="col-sm-1"></div>
            <div class="col-sm-10">
                <div id="quizes" class="table-responsive">
                    <table id="question-table" class="table">
                        <thead>
                        <tr>
                            <th class="col-md-1" data-field="id">#</th>
                            <th class="col-md-1" data-field="text">Question</th>
                            <th class="col-md-1" data-field="choices">Answers</th>
                            <th class="col-md-1" data-field="button" data-formatter="deleteQuestionButtonFormatter" id="deleteQuestionButton">Delete</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
            <div class="col-sm-1"></div>
        </div>
</section>
<!-- add student by email -->
<div class="row">
    <div class="col-sm-4"></div>
    <div class="col-sm-4">
        <form id="quiz-form" method="post" style="margin-top: 30px;">
            <label>Add Quiz</label>
            <div class="form-cotrol">
                <textarea class="form-control" placeholder="Your question *" id="question-text" required data-validation-required-message="Please enter a message."></textarea>
                %{--<input id="quiz" type="text" placeholder="what is your question?" required>--}%
                %{--<input type="submit" value="Add Quiz" class="btn btn-success" id="email-button">--}%

                <br>
                <br>
                <label>A</label>
                <input id="answer-a" type="text" placeholder="Answer option A?" required>  <input type="checkbox" name="A" value="">
                <br>
                <br>
                <label>B</label>
                <input id="answer-b" type="text" placeholder="Answer option B?" required>  <input type="checkbox" name="B" value="">
                <br>
                <br>
                <label>C</label>
                <input id="answer-c" type="text" placeholder="Answer option C?" required> <input type="checkbox" name="C" value="">
                <br>
                <br>
                <label>D</label>
                <input id="answer-d" type="text" placeholder="Answer option D?" required> <input type="checkbox" name="D" value="">
                <br>
                <br>
                <label>E</label>
                <input id="answer-e" type="text" placeholder="Answer option E?" required> <input type="checkbox" name="E" value="">
            </div>
        </form>
    </div>
</div>

<div class="row">
    <div class="col-md-4 col-lg-offset-3 text-center">
        <input type="submit" value="Add Question" data-course-id="${session.courseId}" data-quiz-id="${session.quizId}" class="btn btn-success" id="create-question-btn">
    </div>
    <div class="col-md-1 text-center">
        <a class="btn btn-success" href="/course/quizList?courseId=${session.courseId}">Submit Quiz</a>
    </div>
</div>

<!-- Modal -->
<div id="deleteQuestionModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content clean-container">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title heading">Delete Question?</h4>
            </div>
            <div class="modal-body">
                <p>Are you sure you want to remove this question from the quiz?</p>
            </div>
            <div class="modal-footer">
                <button id="confirmDeleteQuestion" type="button" class="btn btn-danger btn-ok js-deleteQuestion" data-dismiss="modal" data-question-id="">Yes</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
            </div>
        </div>
    </div>
</div>

</div>




<asset:javascript src="jquery-3.2.0.min.js"/>
<script src="https://apis.google.com/js/platform.js"></script>

<!-- Latest compiled and minified JavaScript -->
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>

<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
<asset:javascript src="instructor.js"/>
<asset:javascript src="createAtHome.js"/>
<script>
    window.onload=prepareClassTitle(${session.courseId});
    window.onload=setQuizId(${session.quizId});
</script>

</body>
</html>