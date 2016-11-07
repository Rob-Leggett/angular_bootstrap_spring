'use strict';

angular.module('app.services').service('storageService', ['$rootScope', '$window', function ($rootScope, $window) {
    this.getLocalItem = function (key) {
        return JSON.parse($window.localStorage.getItem(key));
    };

    this.setLocalItem = function (key, item) {
        $window.localStorage.setItem(key, JSON.stringify(item));
    };

    this.removeLocalItem = function(key) {
        $window.localStorage.removeItem(key);
    };

    this.getSessionItem = function (key) {
        return JSON.parse($window.sessionStorage.getItem(key));
    };

    this.setSessionItem = function (key, item) {
        $window.sessionStorage.setItem(key, JSON.stringify(item));
    };

    this.removeSessionItem = function(key) {
        $window.sessionStorage.removeItem(key);
    }
}]);