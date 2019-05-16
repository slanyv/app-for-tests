<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Space Mission Manager</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.6/angular.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.6/angular-resource.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.6/angular-route.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.6/angular-cookies.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/angular_app.js"></script>
    <script src="${pageContext.request.contextPath}/authenticationService.js"></script>
    <script src="${pageContext.request.contextPath}/service.js"></script>
    <script src="${pageContext.request.contextPath}/controllers/LoginCtrl.js"></script>
    <script src="${pageContext.request.contextPath}/controllers/ComponentsCtrl.js"></script>
    <script src="${pageContext.request.contextPath}/controllers/MissionsCtrl.js"></script>
    <script src="${pageContext.request.contextPath}/controllers/SpacecraftsCtrl.js"></script>
    <script src="${pageContext.request.contextPath}/controllers/UsersCtrl.js"></script>


</head>
<body ng-app="spaceMissionApp">
<!-- navigation bar -->
<nav class="navbar navbar-inverse navbar-static-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">Space Mission Manager</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li ng-if="user.manager" id="users"><a href="#!/users">Users</a></li>
                <li ng-if="user.manager" id="missions"><a href="#!/missions">Missions</a></li>
                <li ng-if="user.manager" id="spacecrafts"><a href="#!/spacecrafts">Spacecrafts</a></li>
                <li ng-if="user.manager" id="components"><a href="#!/components">Craft components</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown" ng-if="globals.currentUser">
                    <a class="dropdown-toggle" type="button" id="dropdownMenu1"
                       data-toggle="dropdown">
                        {{globals.currentUser.email}}
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
                        <li><a href="#" ng-click="logout()">Logout</a></li>
                        <li><a href="#!/profile">My profile</a></li>
                    </ul>

                </li>

                <li ng-if="!globals.currentUser"><a href="#"><span class="glyphicon glyphicon-log-in"></span> Login</a>
                </li>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
</nav>

<div class="container">

    <div>

        <!-- Bootstrap-styled alerts, visible when $rootScope.xxxAlert is defined -->
        <div ng-show="warningAlert" class="alert alert-warning alert-dismissible" role="alert">
            <button type="button" class="close" aria-label="Close" ng-click="hideWarningAlert()"><span
                    aria-hidden="true">&times;</span></button>
            <strong>Warning!</strong> <span>{{warningAlert}}</span>
        </div>
        <div ng-show="errorAlert" class="alert alert-danger alert-dismissible" role="alert">
            <button type="button" class="close" aria-label="Close" ng-click="hideErrorAlert()"><span aria-hidden="true">&times;</span>
            </button>
            <strong>Error!</strong> <span>{{errorAlert}}</span>
        </div>
        <div ng-show="successAlert" class="alert alert-success alert-dismissible" role="alert">
            <button type="button" class="close" aria-label="Close" ng-click="hideSuccessAlert()"><span
                    aria-hidden="true">&times;</span></button>
            <strong>Success !</strong> <span>{{successAlert}}</span>
        </div>


        <div ng-if="user.mission" class="alert alert-success" role="alert">

            <strong ng-if="user.acceptedMission">Accepted mission:</strong>
            <div ng-if="!user.acceptedMission"><strong>New Mission!</strong> <span>You have new mission request! Do you want to accept it?</span>
            </div>
            <span>
                <br>Name: {{user.mission.name}}
                <br>Destination: {{user.mission.destination}}
                <br>Description: {{user.mission.missionDescription}}
            </span>
            <br><br>
            <div ng-if="!decline.declined && !user.acceptedMission">
                <button class="btn btn-success" ng-click="acceptMission()">Accept</button>
                <button class="btn btn-danger" ng-click="decline.declined = true">Decline</button>
            </div>
            <div ng-if="decline.declined && !user.acceptedMission">
                <form>
                    <div class="form-group">
                        <label for="explanation">Message</label>
                        <input type="text" minlength="10" class="form-control" id="explanation"
                               ng-model="decline.message" placeholder="Enter message">
                    </div>
                    <button type="submit" class="btn btn-success" ng-click="declineMission()">Send declinaion</button>
                    <button type="submit" ng-click="decline.declined = false" class="btn btn-danger">Cancel</button>

                </form>
            </div>
        </div>

        <!-- the place where HTML templates are replaced by AngularJS routing -->
        <div ng-view></div>
    </div>
</div>
</body>
</html>