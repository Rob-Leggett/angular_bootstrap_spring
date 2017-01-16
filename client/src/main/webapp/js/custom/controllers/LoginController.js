'use strict';

angular.module('app.controllers').controller('LoginController', ['$location', '$rootScope', '$scope', 'authenticationService', 'messageService',
  function ($location, $rootScope, $scope, authenticationService, messageService) {
    $scope.login = function (credentials) {
        authenticationService.login(credentials)
            .then(function() {
                $location.path($rootScope.navigateTo);
            })
            .catch(function () {
                messageService.error("LOGIN_FAILURE", "We were unable to log you in, please try again.");
            });
    };
}]);