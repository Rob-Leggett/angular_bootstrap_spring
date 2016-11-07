'use strict';

angular.module('app.controllers').controller('MenuController', ['$location', '$scope', 'authenticationService', 'messageService', function ($location, $scope, authenticationService, messageService) {
    $scope.logout = function logout() {
        authenticationService.logout()
            .then(function() {
                $location.path("/logout");
            })
            .catch(function () {
                messageService.error("LOGOUT_FAILURE", "We were unable to log you out, please try again.");
            });
    };
}]);