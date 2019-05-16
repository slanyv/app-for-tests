spaceMissionApp.factory('$spaceHttp', ['$http', function ($http) {
    var service = {};

    const PORT_NUMBER = 8081;
    const API_URL = "http://localhost:" + PORT_NUMBER + "/pa165/rest/";
    const LOGIN_PATH = "login";
    const CC_PATH = "craftComponents";
    const USERS_PATH = "users";
    const MISSIONS_PATH = "missions";
    const ASTRONAUTS_PATH = USERS_PATH + "/astronauts";
    const SPACECRAFTS_PATH = "spacecrafts";


    service.login = function (name, password) {
        return $http.post(API_URL+LOGIN_PATH, {name: name, password: password});
    };

    //Users
    service.getAllUsers = function () {
        return $http.get(API_URL+USERS_PATH);
    };

    service.deleteUser = function (id) {
        return $http.delete(API_URL+USERS_PATH+'/'+id);
    };


    service.getUser = function (id) {
        return $http.get(API_URL+USERS_PATH+'/'+id);
    };

    service.updateUser = function (data){
        return $http.put(API_URL+USERS_PATH, data);
    };

    service.updateProfile = function (data){
        return $http.put(API_URL+USERS_PATH+"/profile", data);
    };

    service.createUser = function (data){
        return $http.post(API_URL+USERS_PATH, data);
    };

    service.getUserMission = function (id){
        return $http.get(API_URL+USERS_PATH+"/mission/"+id);
    };


    service.loadAstronauts = function () {
        return $http.get(API_URL+ASTRONAUTS_PATH);
    };

    service.loadAvailableAstronauts = function () {
        return $http.get(API_URL+ASTRONAUTS_PATH+"/available");
    };

    service.acceptMission = function (id) {
        return $http.get(API_URL+USERS_PATH+'/'+id+'/acceptMission');
    };

    service.declineMission = function (id, message) {
        return $http.post(API_URL+USERS_PATH+'/'+id+'/rejectMission', message);
    };

    //Missions
    service.createMission = function (data){
        return $http.post(API_URL+MISSIONS_PATH, data);
    };

    service.loadMissions = function () {
        return $http.get(API_URL+MISSIONS_PATH);
    };

    service.loadActiveMissions = function () {
        return $http.get(API_URL+MISSIONS_PATH+"?active=true");
    };
    service.loadInactiveMissions = function () {
        return $http.get(API_URL+MISSIONS_PATH+"?active=false");
    };

    service.deleteMission = function (id) {
        return $http.delete(API_URL+MISSIONS_PATH+'/'+id);
    };

    service.getMission = function (id) {
        return $http.get(API_URL+MISSIONS_PATH+'/'+id);
    };

    service.updateMission = function (data){
        return $http.put(API_URL+MISSIONS_PATH, data);
    };

    service.archiveMission = function (id) {
	    return $http.post(API_URL+MISSIONS_PATH+'/'+id+'/archive');
    };

    //Spacecrafts
    service.loadSpacecrafts = function () {
        return $http.get(API_URL+SPACECRAFTS_PATH);
    };

    service.loadAvailableSpacecrafts = function() {
        return $http.get(API_URL+SPACECRAFTS_PATH+"/available");
    };

    service.loadComponents = function () {
	    return $http.get(API_URL+CC_PATH);
    };

    service.createComponent = function (cc) {
    	console.log("creating cc");
	    return $http.post(API_URL+CC_PATH, cc);
    };

    service.updateComponent = function (cc) {
	    console.log("updating cc"+cc);
	    return $http.put(API_URL+CC_PATH, cc);
    };

    service.deleteComponent = function (id) {
	    console.log("deleting cc");
	    return $http.delete(API_URL+CC_PATH+"/"+id);
    };

    service.getComponent = function (id) {
	    return $http.get(API_URL+CC_PATH+"/"+id);
    };

    service.getAllSpacecrafts = function () {
        return $http.get(API_URL+SPACECRAFTS_PATH);
    };

    service.getSpacecraft = function (id) {
        return $http.get(API_URL+SPACECRAFTS_PATH+'/'+id);
    };

    service.createSpacecraft = function (data){
        return $http.post(API_URL+SPACECRAFTS_PATH, data);
    };

    service.deleteSpacecraft = function (id) {
        return $http.delete(API_URL+SPACECRAFTS_PATH+'/'+id);
    };

    service.updateSpacecraft = function (data) {
        return $http.put(API_URL+SPACECRAFTS_PATH,data);
    };

    service.getAllAvailableCraftComponents = function () {
        return $http.get(API_URL+CC_PATH+'/available')
    };


    return service;
}]);