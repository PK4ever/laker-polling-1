<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Instructor</title>
    <asset:stylesheet href="bootstrap.min.css"/>
    <asset:stylesheet href="bootstrap-theme.min.css"/>
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
                <h2 style="text-align: center">Add a new course</h2>
                <form id="addClassForm" role="form">
                    <div class="form-group">
                        <label for="courseName">Course name</label>
                        <input type="text" class="form-control" id="courseName" placeholder="CSC212" required>
                    </div>
                    <div class="form-group">
                        <label for="courseCRN">CRN</label>
                        <input type="text" class="form-control" id="courseCRN" placeholder="133742" required>
                    </div>
                </form>
                <div class="form-group">
                    <button type="submit" class="btn btn-success" id="courseButton">Create Course</button>
                </div>
            </div>
            </div>
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
<asset:javascript src="instructor.js"/>
</body>
</html>