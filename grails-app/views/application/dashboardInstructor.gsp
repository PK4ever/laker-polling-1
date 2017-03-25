<!DOCTYPE html>
<html lang="en">
<head>
        <meta charset="UTF-8">
        <title>Instructor Dashboard</title>
        <asset:stylesheet href="bootstrap.min.css"/>
        <asset:stylesheet href="bootstrap-theme.min.css"/>
        <asset:stylesheet href="teacher.css"/>
    </head>
    <body>
        <div class="navbar navbar-default" role="navigation">
            <div class="navbar-header">
                <asset:image class="img-responsive navbar-brand" src="logo.png"/>
                <a class="navbar-brand">Instructor Dashboard</a>
            </div>
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <button onclick="logout()" class="btn btn-default navbar-right navbar-btn">Logout</button>
                </ul>
            </div>
        </div>
        <div class="container">
            <div class="row">
                <div class="col-sm-4"></div>
                <div class="col-sm-4">
                    <div id="courses" class="table-responsive">
                        <table id="courseTable" class="table">
                            <thead>
                                <tr>
                                    <th class="col-md-3" data-field="name" data-formatter="identifierFormatter">Course Name</th>
                                    <th class="col-md-1" data-field="crn">CRN</th>
                                    <th class="col-md-1" data-field="students">Number of Students</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
                <div class="col-sm-4"></div>
            </div>
            <div class="row">
                <div class="col-sm-4"></div>
                <div class="col-sm-4">
                    <button id="courseButton">Create Course</button>
                </div>
                <div class="col-sm-4"></div>
            </div>
        </div>

        <asset:javascript src="jquery-3.2.0.min.js"/>
        <script src="https://apis.google.com/js/platform.js"></script>

        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.css">

        <!-- Latest compiled and minified JavaScript -->
        <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>

        <asset:javascript src="auth/config.js"/>
        <asset:javascript src="auth/logout.js"/>
        <asset:javascript src="student.js"/>
    </body>
</html>