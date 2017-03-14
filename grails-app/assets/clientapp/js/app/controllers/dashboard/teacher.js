angularApp
    //a controller to display the data to appropriately list views 
    // allows us to add data to the scope and access it from our views.
    .controller("TeacherDashboardController", function($scope, $window, $location, $routeParams, $timeout, currentUser) {
        $scope.currentUser = currentUser

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

        //add course
        $scope.addCourse = function(crn,name) {
          $http({
            method: 'POST',
            url: 'http://localhost:8080/api/course/create?accessToken=' + currentUser.getAccessToken() + '&userId=' + currentUser.getId() + '&crn=' + crn + '&name=' + name
          })
          .then(function(response) {
            $scope.courses = response.data.courses;
          });
        }

        //delete course
        $scope.deleteCourse = function(id){
          $http({
            method: 'DELETE'
            url: 'http://localhost:8080/api/course?accessToken=' + currentUser.getAccessToken()+ '&courseId='+ id
          })
          .then(function(response){
            $scope.courses = response.data.courses;
          });
        }
    })