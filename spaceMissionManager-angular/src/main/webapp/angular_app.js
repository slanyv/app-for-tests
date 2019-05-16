    'use strict';

/* Defines application and its dependencies */

var spaceMissionApp = angular.module('spaceMissionApp', ['ngRoute', 'controllers', 'ngCookies']);
var controllers = angular.module('controllers', []);

/* Configures URL fragment routing, e.g. #/product/1  */
spaceMissionApp.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.
        when('/login', {templateUrl: 'views/login.html', controller: 'LoginCtrl'}).
        when('/users', {templateUrl: 'views/users.html', controller: 'UsersCtrl'}).
        when('/profile', {templateUrl: 'views/users.html', controller: 'UsersCtrl'}).
        when('/components', {templateUrl: 'views/components.html', controller: 'ComponentsCtrl'}).
        when('/missions', {templateUrl: 'views/missions.html', controller: 'MissionsCtrl'}).
        when('/spacecrafts', {templateUrl: 'views/spacecrafts.html', controller: 'SpacecraftsCtrl'}).
        otherwise({redirectTo: '/login'});
    }]);

/*
 * alert closing functions defined in root scope to be available in every template
 */
spaceMissionApp.run(function ($rootScope, AuthenticationService, $spaceHttp, $location) {
    var storedUserInfo = localStorage.getItem('user');
    if(storedUserInfo){
        var user = JSON.parse(storedUserInfo);
        $rootScope.user = user;

        AuthenticationService.SetCredentials(user.email, user.rawPassowrd);
        AuthenticationService.Login(user.email, user.rawPassowrd).then(
            function (response) {
                var rp = $rootScope.user.rawPassowrd;
                $rootScope.user = response.data;
                var user = response.data;
                user.rawPassowrd = rp;
                localStorage.setItem('user', JSON.stringify(user));
                $spaceHttp.getUserMission(user.id).then(function (res) {
                     $rootScope.user.mission = res.data;
                });
            },
            function (error) {
                console.log(error);
            }
        );
    }

    $rootScope.hideSuccessAlert = function () {
        $rootScope.successAlert = undefined;
    };
    $rootScope.hideWarningAlert = function () {
        $rootScope.warningAlert = undefined;
    };
    $rootScope.hideErrorAlert = function () {
        $rootScope.errorAlert = undefined;
    };

    $rootScope.decline = {};

    $rootScope.acceptMission = function(){
        $spaceHttp.acceptMission($rootScope.user.id).then(function (response) {
            $rootScope.successAlert = 'Mission accepted!';
            var mission = $rootScope.user.mission;
            $rootScope.user = response.data;
            $rootScope.user.mission = mission;
        }, function (error) {
            console.log(error);
        });
    }
    $rootScope.declineMission = function(){
        $spaceHttp.declineMission($rootScope.user.id, JSON.stringify($rootScope.decline.message)).then(function (response) {
            $rootScope.successAlert = 'Mission declined!';
            var mission = $rootScope.user.mission;
            $rootScope.user = response.data;
            $rootScope.user.mission = mission;
        }, function (error) {
            console.log(error);
        });
    }

    $rootScope.logout = function(){
        AuthenticationService.ClearCredentials();
        $location.path('login');
    };

});

