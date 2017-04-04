$(document).ready(function () {
    gapi.load('auth2', function () {
        var auth2 = gapi.auth2.init({
            client_id: _clientid,
            scope: 'profile'
        });

        function doLogOut() {
            auth2.signOut().then(function () {
                document.location.href = "/";
            });
        }

        window.logout = function () {

            $.ajax({
                url: '/user/logout',
                method: "POST",
                success: doLogOut(),
                error: doLogOut()
            });
        }

    });
});