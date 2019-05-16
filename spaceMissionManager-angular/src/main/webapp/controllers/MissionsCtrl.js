controllers.controller('MissionsCtrl', function ($scope, $spaceHttp, $rootScope, $location) {

    if (typeof $rootScope.globals === 'undefined' || typeof $rootScope.globals.currentUser === 'undefined' || !$rootScope.user.manager) {
        $location.path('login');
        return;
    }

	$rootScope.errorAlert = '';
	$rootScope.successAlert = '';
	$rootScope.warningAlert ='';
    $scope.date = new Date().toISOString().substring(0,16);
    console.log('calling  /missions');
    $spaceHttp.loadActiveMissions().then(function (response) {
        console.log(response);
        $scope.missions = response.data;
    });

    $spaceHttp.loadInactiveMissions().then(function (response) {
	    console.log(response);
	    $scope.archivedMissions = response.data;
    });

    $scope.createNewMission = function () {
        $scope.editedMission =  {};
        $scope.create = true;
        $spaceHttp.loadAvailableAstronauts().then(function (value) {
            $scope.selectedAstronauts = value.data
        });
        $spaceHttp.loadAvailableSpacecrafts().then(function (value) {
            $scope.selectedSpacecrafts = value.data
        })
    };

    $scope.submitCreate = function () {
        var data = angular.copy($scope.editedMission);
        data.active = true;
        $spaceHttp.createMission(data).then(function (res) {
            $spaceHttp.loadActiveMissions().then(function (response) {
                $scope.missions = response.data;
                $scope.create = false;
            }, function (error) {
                console.error(error);
            });
            $rootScope.successAlert = 'A new mission "' + data.name +'" was created';
	        $scope.selectedSpacecraft = null;
	        $scope.selectedMission = null;
        }, function (error) {
            console.error(error);
            $rootScope.errorAlert = 'Cannot create mission!';
        })
    };

    $scope.deleteMission = function (id) {
        $spaceHttp.deleteMission(id).then(function (response) {
	        $rootScope.errorAlert = '';
	        $rootScope.successAlert = 'Removing was successful';
	        $scope.selectedSpacecraft = null;
	        $scope.selectedMission = null;
	        $spaceHttp.loadActiveMissions().then(function (response) {
		        $scope.missions = response.data;
	        });
	        $spaceHttp.loadInactiveMissions().then(function (response) {
		        $scope.archivedMissions = response.data;
	        });
        }, function (error) {
            console.error(error);
	        $rootScope.errorAlert = 'Cannot delete mission!';
	        $rootScope.successAlert = '';
        })
    };

    $scope.editMission = function (id) {
        $spaceHttp.getMission(id).then(function (response) {
            $scope.editedMission = response.data;
            $spaceHttp.loadAvailableAstronauts().then(function (value) {
                $scope.selectedAstronauts = value.data.concat(response.data.astronauts);
            });
            $spaceHttp.loadAvailableSpacecrafts().then(function (value) {
                $scope.selectedSpacecrafts = value.data.concat(response.data.spacecrafts);
            });
            $scope.editedMission.eta = $scope.editedMission.eta === null || $scope.editedMission.eta === undefined
                ? null : new Date($scope.editedMission.eta.substring(0, 16));
            $scope.edit = true;
        }, function (error) {
            console.error(error);
        })
    };

    $scope.cancelEdit = function () {
        $scope.create = false;
        $scope.edit = false;
        $scope.view = false;
	    $scope.selectedSpacecraft = null;
	    $scope.selectedMission = null;
    };

    $scope.submitEdit = function () {
        var data = angular.copy($scope.editedMission);
        $spaceHttp.updateMission(data).then(function (res) {
            $spaceHttp.loadActiveMissions().then(function (response) {
                $scope.missions = response.data;
                $scope.edit = false;
            }, function (error) {
                console.error(error);
            });
            $rootScope.successAlert = 'The "' + data.name +'" mission was successfully edited';
	        $scope.selectedSpacecraft = null;
	        $scope.selectedMission = null;
        }, function (error) {
            console.error(error);
            $rootScope.errorAlert = 'Cannot update mission!';
        })
    };



    $scope.selectedMission = null;
    $scope.setSelected = function (id) {
        $spaceHttp.getMission(id).then(function (response) {
            $scope.selectedMission = response.data;
	        $scope.selectedSpacecraft = null;
        }, function (error) {
	        console.error(error);
	        $rootScope.errorAlert = 'Unable to select mission';
	        $rootScope.successAlert = '';
        })
    };

	$scope.selectedSpacecraft = null;
	$scope.setSelectedS = function (id) {
		$spaceHttp.getSpacecraft(id).then(function (response) {
			$scope.selectedSpacecraft = response.data;
		}, function (error) {
			console.error(error);
			$rootScope.errorAlert = 'Unable to select spacecraft';
			$rootScope.successAlert = '';
		})
	};

	$scope.archiveMission = function (id) {
		$spaceHttp.archiveMission(id).then(function (response) {

			$spaceHttp.loadActiveMissions().then(function (response) {
				console.log(response);
				$scope.missions = response.data;
				$scope.selectedMission = null;
				$scope.selectedSpacecraft = null;
			});

			$spaceHttp.loadInactiveMissions().then(function (response) {
				console.log(response);
				$scope.archivedMissions = response.data;
			});

		});
	};

	$scope.viewMission = function (id) {
		$spaceHttp.getMission(id).then(function (response) {
			$scope.editedMission = response.data;
			$scope.view = true;
		}, function (error) {
			console.error(error);
		})
	};



});