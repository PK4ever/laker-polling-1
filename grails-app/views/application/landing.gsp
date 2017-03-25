<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Laker Polling</title>
        <asset:stylesheet href="bootstrap.min.css"/>
        <asset:stylesheet href="bootstrap-theme.min.css"/>
    </head>
    <body>
        <div class="container" style="max-width:850px">
            <div class="row">
                <div class="span4"></div>
                <div class="span4">
                    <asset:image src="logo.png" class="img-responsive center-block" id="logo"/>
                    <div align="center" id="sign-in-btn"></div>
                </div>
                <div class="span4"></div>
            </div>
        </div>

        <asset:javascript src="jquery-3.2.0.min.js"/>
        <script src="https://apis.google.com/js/platform.js"></script>
        <asset:javascript src="auth/config.js"/>
        <asset:javascript src="auth/login.js"/>
        <asset:stylesheet src="login.css"/>
    </body>
</html>