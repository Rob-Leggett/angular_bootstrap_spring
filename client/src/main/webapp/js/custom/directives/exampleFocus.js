'use strict';

angular.module('app.directives').directive('exampleFocus',
    function ($timeout) {
    return {
        scope: {
            trigger: '@exampleFocus'
        },
        link: function (scope, element) {
            scope.$watch('trigger', function () {
                $timeout(function () {
                    element[0].focus();
                });
            });
        }
    };
});
