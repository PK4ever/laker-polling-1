$(document).ready(function () {

    function signOut() {
        var auth2 = gapi.auth2.getAuthInstance();
        auth2.signOut();
    }

    function authSuccess(data) {
        console.log(data);
        debugger;
        if (data.status) {
            document.location.href = "/dashboard";
        } else {
            signOut();
        }
    }

    function authFailure(xhr, textStatus, errorThrown) {
        var responseText = xhr.responseText;
        if(responseText != null) {
            var status;
            try {
                status = JSON.parse(responseText);
            }
            catch(e) {
               status = undefined;
            }

            if(status && status.message) {
                alert(status.message);
            }
        }
        signOut();
    }

    function send(token) {
        $.ajax({
            url: '/user/auth',
            method: "POST",
            data: {
                idToken: token
            },
            success: authSuccess,
            error: authFailure
        });
    }

    function renderButton() {
        gapi.signin2.render('sign-in-btn', {
            'width': 240,
            'height': 50,
            'longtitle': true,
            'theme': 'dark'
        });
    }

    gapi.load('auth2', function () {
        var auth2 = gapi.auth2.init({
            client_id: _clientid,
            scope: 'profile'
        });

        auth2.currentUser.listen(function (user) {
            if (auth2.isSignedIn.get() == true) {
                var token = user.getAuthResponse().id_token;
                send(token);
            }
        });

        renderButton();

    });
});
