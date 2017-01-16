'use strict';

angular.module('app.services').service('userService', ['$http', '$q', 'propertiesConstant', function ($http, $q, propertiesConstant) {
    this.retrieve = function retrieve() {
        var d = $q.defer();

        $http.get(propertiesConstant.API_URL + '/user')
            .then(function success(response) {
                d.resolve(response.data);
            }, function error() {
                d.reject();
            });

        return d.promise;
    };
}]);