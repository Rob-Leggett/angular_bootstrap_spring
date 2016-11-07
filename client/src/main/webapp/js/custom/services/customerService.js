'use strict';

angular.module('app.services').service('customerService', [ '$http', '$q', 'propertiesConstant', function ($http, $q, propertiesConstant) {
    this.getCustomers = function () {
        var d = $q.defer();

        $http.get(propertiesConstant.API_URL + '/customer/customers/retrieve')
            .success(function (customers) {
                d.resolve(customers);
            })
            .error(function (data, status, headers, config) {
                d.reject(status);
            });

        return d.promise;
    };

    this.deleteCustomer = function (id) {
        var d = $q.defer();

        $http.delete(propertiesConstant.API_URL + '/customer/delete/' + id)
            .success(function (response) {
                d.resolve(response);
            })
            .error(function () {
                d.reject();
            });

        return d.promise;
    };

    this.saveCustomer = function (customer) {
        var d = $q.defer();

        $http.post(propertiesConstant.API_URL + '/customer/save', customer)
            .success(function (response) {
                d.resolve(response);
            })
            .error(function () {
                d.reject();
            });

        return d.promise;
    };
}]);