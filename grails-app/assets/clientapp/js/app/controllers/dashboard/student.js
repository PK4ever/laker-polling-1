angularApp
    //a controller to display the data to appropriately list views 
    // allows us to add data to the scope and access it from our views.
    .controller("StudentDashboardController", function($scope, $window, $location, $routeParams) {
        debugger
        $scope.currentUser = User.getCurrent();

        //get courses
        function($http){
          $http({
            method: 'GET',
            url: 'http://localhost:8080/api/course?accessToken=' + currentUser.getAccessToken()
          })
          .then(function(response) {
            $scope.courses = response.data.courses;
          });
        }
    })