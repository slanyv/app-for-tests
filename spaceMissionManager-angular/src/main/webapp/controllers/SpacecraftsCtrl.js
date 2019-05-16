
controllers.controller('SpacecraftsCtrl', function ($scope, $spaceHttp, $rootScope, $location) {

    if (typeof $rootScope.globals === 'undefined' || typeof $rootScope.globals.currentUser === 'undefined' || !$rootScope.user.manager) {
        $location.path('login');
        return;
    }

	$rootScope.errorAlert = '';
	$rootScope.successAlert = '';
	$rootScope.warningAlert ='';



    console.log('calling /spacecrafts');
    $spaceHttp.getAllSpacecrafts().then(function (response) {
        $scope.spacecrafts = response.data;
    }, function (error) {
        console.error(error);
    });

    $scope.deleteSpacecraft = function (id) {
        $spaceHttp.deleteSpacecraft(id).then(function (response) {
            $scope.spacecrafts = response.data;
            $rootScope.successAlert = 'Removing was successful'
	        $rootScope.errorAlert = '';
	        $scope.selectedSpacecraft = null;

        }, function (error) {
            console.error(error);
	        $rootScope.errorAlert = 'Cannot delete spacecraft!';
	        $rootScope.successAlert = '';
        })
    };

    $scope.editSpacecraft = function (id) {
        $spaceHttp.getSpacecraft(id).then(function (response) {
            $scope.editedSpacecraft = response.data;
            $spaceHttp.getAllAvailableCraftComponents().then(function (res) {
               $scope.availableAndSelectedCC = res.data.concat(response.data.components);
            });
            $scope.edit = true;
        }, function (error) {   
            console.error(error);
        })
    };

    $scope.submitEdit = function () {
        var data = angular.copy($scope.editedSpacecraft);
        $spaceHttp.updateSpacecraft(data).then(function (res) {
            $spaceHttp.getAllSpacecrafts().then(function (response) {
                $scope.spacecrafts = response.data;
                $scope.edit = false;
            }, function (error) {
                console.error(error);
            });
            $rootScope.successAlert = 'A new spacecraft "' + data.name +'" was created';
	        $rootScope.errorAlert = '';
	        $scope.selectedSpacecraft = null;

        }, function (error) {
            console.error(error);
	        $rootScope.successAlert = '';
	        $rootScope.errorAlert = 'Cannot update spacecraft!';

        })
    };

    $scope.createSpacecraft = function () {
        $scope.editedSpacecraft = {};
        $scope.create = true;
        $spaceHttp.getAllAvailableCraftComponents().then(function (value) {
            $scope.availableAndSelectedCC = value.data
        })
    };

    $scope.submitCreate = function () {
        var data = angular.copy($scope.editedSpacecraft);
        $spaceHttp.createSpacecraft(data).then(function (res) {
            $spaceHttp.getAllSpacecrafts().then(function (response) {
                $scope.spacecrafts = response.data;
                $scope.create = false;
            }, function (error) {
                console.error(error);
            });
            $rootScope.successAlert = 'A new spacecraft "' + data.name +'" was created'
	        $rootScope.errorAlert = '';
	        $scope.selectedSpacecraft = null;

        }, function (error) {
            console.error(error);
            $rootScope.errorAlert = 'Cannot create spacecraft!';
	        $rootScope.successAlert = '';

        })
    };

    $scope.cancelEdit = function () {
        $scope.create = false;
        $scope.edit = false;
	    $rootScope.errorAlert = '';
	    $rootScope.successAlert = '';
    };

	$scope.selectedSpacecraft = null;
	$scope.setSelected = function (id) {
		$spaceHttp.getSpacecraft(id).then(function (response) {
			$scope.selectedSpacecraft = response.data;
		}, function (error) {
			console.error(error);
			$rootScope.errorAlert = 'Unable to select spacecraft';
			$rootScope.successAlert = '';
		})
	}
});
