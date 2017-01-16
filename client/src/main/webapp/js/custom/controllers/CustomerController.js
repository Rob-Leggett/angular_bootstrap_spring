'use strict';

angular.module('app.controllers').controller('CustomerController',['$rootScope', '$scope', '$location', 'customerService', 'messageService',
  function ($rootScope, $scope, $location, customerService, messageService) {
    customerService.getCustomers().then(
        function success(customers) {
            $scope.customers = customers;
        },
        function error(status) {
            if(status === 401) {
                $location.path("/login");
            }
            else {
                messageService.error("CUSTOMERS_GET_FAILURE", "Oooooops something went wrong, please try again");
            }
        });

    $scope.remove = function remove(id) {
        customerService.deleteCustomer(id).then(
            function success(response) {
                if (response) {
                    angular.forEach($scope.customers, function (customer, index) {
                        if (id == customer.id) {
                            $scope.customers.splice(index, 1);
                        }
                    });
                }
            },
            function error() {
                messageService.error("CUSTOMER_DELETE_FAILURE", "Oooooops something went wrong, please try again");
            });
    };

    $scope.save = function (id) {
        angular.forEach($scope.customers, function (customer) {
                if (id == customer.id) {
                    customerService.saveCustomer(customer).then(
                        function success(response) {});
                }
            },
            function error() {
                messageService.error("CUSTOMER_SAVE_FAILURE", "Oooooops something went wrong, please try again");
            });
    };
}]);