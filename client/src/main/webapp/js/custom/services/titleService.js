'use strict';

angular.module('app.services').service('titleService', ['$route', '$window', function ($route, $window) {

    var title = $window.document.title;

    this.changeTitle = function () {
        $window.document.title = title + " - " + $route.current.title;
    };
}]);