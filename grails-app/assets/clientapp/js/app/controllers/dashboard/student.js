angularApp
    //a controller to display the data to appropriately list views
    // allows us to add data to the scope and access it from our views.
    .controller("StudentDashboardController", function($scope, $window, $location, $routeParams, $http) {
        debugger
        $scope.currentUser = User.getCurrent();


            console.log($scope.currentUser.getAccessToken());
        //get courses
         $http({
         method: 'GET',
         url: '/api/course?access_token=' + $scope.currentUser.getAccessToken()
//         url: '/api/course?access_token=ee'
         })
          .then(function(response) {
            console.log(response.data);
            $scope.courses = response.data.courses;
//            $scope.currentUser.setToken(response.data.token);
          });
    })