
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin</title>
</head>
<body>
    <div class="navbar navbar-default" role="navigation">
        <div class="navbar-header">
            <asset:image src="logo2.png" class="logo" />
            <a class="navbar-brand">Admin Dashboard</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <a onclick="logout()" class="btn btn-default navbar-right navbar-btn logout">Logout</a>
            </ul>
        </div>
    </div>

<asset:javascript src="jquery-3.2.0.min.js"/>
<script src="https://apis.google.com/js/platform.js"></script>
<asset:javascript src="auth/config.js"/>
<asset:javascript src="auth/logout.js"/>
</body>
</html>