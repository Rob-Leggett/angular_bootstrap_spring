'use strict';

angular.module('app').run(['$rootScope', '$http', '$location', 'titleService', function ($rootScope, $http, $location, titleService) {

    $rootScope.navigateTo = "/main";

    $rootScope.$on('$routeChangeSuccess', function(event, next, current) {
        titleService.changeTitle();

        if ($location.path().indexOf("/login") == -1) {
            $rootScope.navigateTo = $location.path();
        }
    });
}]);