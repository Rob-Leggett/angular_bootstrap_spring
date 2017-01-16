'use strict';

angular.module('app.services').service('messageService', ['$rootScope',
  function ($rootScope) {
    $rootScope.errors = [];
    $rootScope.alerts = [];

    this.error = function (code, message) {
        $rootScope.errors.push({ code: code, message: message });
    };

    this.info = function (code, message) {
        $rootScope.alerts.push({ code: code, message: message });
    };

    this.clearError = function () {
        $rootScope.errors = [];
    };

    this.clearInfo = function () {
        $rootScope.alerts = [];
    }
}]);