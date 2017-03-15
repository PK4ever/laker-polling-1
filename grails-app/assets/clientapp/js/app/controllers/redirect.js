angularApp
    //a controller to display the data to appropriately list views 
    // allows us to add data to the scope and access it from our views.
    .controller("DashboardRedirectController", function($scope, $window, $location, $routeParams, $timeout, currentUser) {
        if(!currentUser) {
            $window.location.href = '/logout'
        } else if(currentUser.type == 'STUDENT') {
            $location.path('/student')
        } else if (currentUser.type == 'INSTRUCTOR') {
            $location.path('/teacher')
        } else {
            $window.location.href = '/logout'
        }
    })