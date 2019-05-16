controllers.controller('ComponentsCtrl', function ($scope, $spaceHttp, $rootScope, $location, $route) {
	console.log('calling  /components');

	if (typeof $rootScope.globals === 'undefined' || typeof $rootScope.globals.currentUser === 'undefined' || !$rootScope.user.manager) {
		$location.path('login');
		return;
	}

	$rootScope.errorAlert = '';
	$rootScope.successAlert = '';
	$rootScope.warningAlert ='';
	$scope.date = new Date().toISOString().substring(0,16);

	$spaceHttp.loadComponents().then(function (response) {
		console.log(response);
		$scope.craftComponents = response.data;
	});

	$scope.delete = function (id) {
		$spaceHttp.deleteComponent(id).then(function success(response) {
			console.log(response);
			//display confirmation alert
			$rootScope.successAlert = 'Component ' + id + ' was deleted';
			$rootScope.errorAlert = '';
			$scope.craftComponents = response.data;
		}, function error(response) {
			//display error
			console.log(response);
			$rootScope.errorAlert = 'Cannot delete component!';
			$rootScope.successAlert = '';
			// $route.reload();

		})
	};

	$scope.editCC = function (id) {
		$spaceHttp.getComponent(id).then(function success(resp) {
			var tmp = resp.data;
			$scope.cc = {
				'id': id,
				'readyToUse': tmp.readyToUse,
				'name': tmp.name,
				'readyDate': tmp.readyDate === null || tmp.readyDate === undefined ? null : new Date(tmp.readyDate.substring(0, 16))
			};
			$scope.edit = true;
		}, function error(resp) {
			console.log(resp);
		});
	};

	$scope.createCC = function () {
		$scope.create = true;
		$scope.cc = {};
	};

	$scope.save = function () {
		var data = angular.copy($scope.cc);
		if (data.readyToUse){
			data.readyDate = null;
		}
		if (!(data.readyDate === null || data.readyDate === undefined)){
			data.readyDate.setHours(data.readyDate.getHours()+1);
			data.readyDate = data.readyDate.toISOString();
		}
		if ($scope.create) {
			$spaceHttp.createComponent(data).then(function (res) {

				$spaceHttp.loadComponents().then(function (response) {
					$scope.craftComponents = response.data;
					$scope.create = false;
				}, function (error) {
					console.error(error);
				});

				$rootScope.errorAlert = '';
				$rootScope.successAlert = 'Component created!';

			}, function (error) {
				console.error(error);
				$rootScope.errorAlert = 'Cannot create component!';
				$rootScope.successAlert = '';

			})
		} else {
			$spaceHttp.updateComponent(data).then(function (res) {

				$spaceHttp.loadComponents().then(function (response) {
					$scope.craftComponents = response.data;
					$scope.edit = false;
				}, function (error) {
					console.error(error);
				});

				$rootScope.errorAlert = '';
				$rootScope.successAlert = 'Component updated';
			}, function (error) {
				console.error(error);
				$rootScope.errorAlert = 'Cannot update component!';
				$rootScope.successAlert = '';

			})

		}
	};

	$scope.cancel = function () {
		$scope.create = false;
		$scope.edit = false;

		$rootScope.errorAlert = '';
		$rootScope.successAlert = '';
	}


});