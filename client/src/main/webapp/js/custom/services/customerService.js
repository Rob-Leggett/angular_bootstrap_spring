'use strict';

angular.module('app.services').service('customerService', [ '$http', '$q', 'propertiesConstant',
  function ($http, $q, propertiesConstant) {
    this.getCustomers = function () {
        var d = $q.defer();

        $http.get(propertiesConstant.API_URL + '/customer')
            .then(function success(response) {
                d.resolve(response.data);
            }, function error(response) {
                d.reject(response.status);
            });

        return d.promise;
    };

    this.deleteCustomer = function (id) {
        var d = $q.defer();

        $http.delete(propertiesConstant.API_URL + '/customer/' + id)
            .then(function success(response) {
                d.resolve(response.data);
            }, function error() {
                d.reject();
            });

        return d.promise;
    };

    this.saveCustomer = function (customer) {
        var d = $q.defer();

        $http.post(propertiesConstant.API_URL + '/customer', customer)
            .then(function success(response) {
                d.resolve(response.data);
            }, function error() {
                d.reject();
            });

        return d.promise;
    };
}]);