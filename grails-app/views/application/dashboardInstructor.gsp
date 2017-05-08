<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Instructor</title>
    <!-- jQuery (necessary for Bootstrap"s JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <asset:stylesheet href="bootstrap.css"/>
    <asset:stylesheet href="agency.min.css"/>
    <asset:stylesheet href="agency.css"/>
    <asset:stylesheet href="style.css"/>
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
                <asset:image src="logo2.png" class="logo"/>
            </a>
            <a id="pageName" class="navbar-brand" style="position: absolute; left: 45%; font-size: x-large;">Instructor</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li style="border: 1.5px solid #fed136">
                    <a class="logout" onclick="logout()" >LogOut</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<section id="portfolio">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <div id="userName"></div>
                <div id="profilePic"></div>
                <h3 class="section-subheading">Welcome to your dashboard</h3>
                <div id="roleButtonDiv" visibility="visible">
                    <button type="submit" class="btn btn-success" id="roleButton">Change role to Student</button>
                    <button type="button" class="btn btn-success" id="instructorButton" data-toggle="modal" data-target="#createInstructorModal">Create a new Instructor</button>
                </div>
            </div>
        </div>
        <div class="row text-center">
            <br>
            <button type="button" class="btn btn-success" id="course-btn" data-toggle="modal" data-target="#createCourseModal">Create a new Course</button>
        </div>

        <h3 class="text-muted">Your Courses:</h3>
        <div class="row" id="courses">
            %{--courses are displayed here--}%
        </div>

    </div>
    <div class="container">
        <div class="row">
            <div class="col-sm-4"></div>
            <div class="col-sm-4">
                <!--<h2 style="text-align: center">Add a new course</h2>
                <form id="addClassForm" role="form">
                    <div class="form-group">
                        <label for="courseName">Course name</label>
                        <input type="text" class="form-control" id="courseName" placeholder="Enter course name here" required>
                    </div>
                    <div class="form-group">
                        <label for="courseCRN">CRN</label>
                        <input type="text" class="form-control" id="courseCRN" placeholder="Enter CRN here" required>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-success" id="courseButton">Create Course</button>
                    </div>
                </form>-->
            </div>
            <div class="col-sm-4">
                <button class="btn btn-default btn-lg js-enableDeleteCoursesMode">
                    <span class="glyphicon glyphicon-trash"></span> Delete Course(s)
                </button>
                <button style="display: none" class="js-disableDeleteCoursesMode">
                    Done Deleting Courses
                </button>
            </div>
        </div>
        <br>
    </div>
</section>

<!-- Create Course Modal -->
<div id="createCourseModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content clean-container">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title heading">Create Course</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label for="courseName">Course name</label>
                    <input type="text" class="form-control" id="modalCourseName" placeholder="Enter course name here..." required>
                </div>
                <div class="form-group">
                    <label for="courseCRN">CRN</label>
                    <input type="text" class="form-control" id="modalCourseCRN" placeholder="Enter course CRN here..." required>
                </div>
            </div>
            <div class="modal-footer">
                <button id="courseButton" type="button" class="btn btn-success btn-ok js-createCourse" data-dismiss="modal">Create Course</button>
                <button id="courseCreateCancelButton" type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
            </div>
        </div>
    </div>
</div>
</div>

<!-- Instructor Modal -->
<div id="createInstructorModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content clean-container">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title heading">Create Instructor</h4>
            </div>
            <div class="modal-body">
                <p>Enter the email of the instructor you would like to add:<br><br>
                <input id="instEmail" type="email" placeholder="lakernetID@oswego.edu"></p>
            </div>
            <div class="modal-footer">
                <button id="addInstructorButton" type="button" class="btn btn-success btn-ok js-createInstructor" data-dismiss="modal">Make Instructor</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
            </div>
        </div>
    </div>
</div>
</div>

<!-- Delete Course Modal -->
<div id="deleteCourseModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content clean-container">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title heading">Delete Course?</h4>
            </div>
            <div class="modal-body">
                <p>Are you sure you want to delete course #<span id="courseId"></span> <span id="courseName"></span>?</p>
            </div>
            <div class="modal-footer">
                <button id="confirmDeleteButton" type="button" class="btn btn-danger btn-ok js-deleteCourse" data-dismiss="modal" data-course-id="">Yes</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
            </div>
        </div>
    </div>
</div>
</div>

<asset:javascript src="jquery-3.2.0.min.js"/>
<script src="https://apis.google.com/js/platform.js"></script>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.css">

<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
<asset:javascript src="instructor.js"/>
<asset:javascript src="main.js"/>
%{--<asset:stylesheet href="style.css"/>--}%

</body>
</html>