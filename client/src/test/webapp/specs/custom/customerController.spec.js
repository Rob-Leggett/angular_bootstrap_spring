describe('CustomerController Tests', function () {
    beforeEach(module('app.controllers'));
    beforeEach(module(function ($provide) {

            $provide.value('$scope', {
                remove: jasmine.createSpy('$scope.remove'),
                save: jasmine.createSpy('$scope.save')
            });

            $provide.factory('customerService', function ($jasmine) {
                return $jasmine.createPromiseSpyObj(
                    'customerService', [ 'getCustomers', 'deleteCustomer', 'saveCustomer' ]
                );
            });

            $provide.factory('messageService', function ($jasmine) {
                return $jasmine.createPromiseSpyObj(
                    'messageService', [ 'error' ]
                );
            });
        })
    );

    describe('CustomerController Structural Tests', function () {
        it('should have an remove function in scope', inject(function ($scope) {
            expect(angular.isFunction($scope.remove)).toBe(true);
        }));

        it('should have an save function in scope', inject(function ($scope) {
            expect(angular.isFunction($scope.save)).toBe(true);
        }));

    });

    describe('CustomerController Save Tests', function () {
        it('should have not saved any data with service call returning true', inject(function ($controller, $scope, customerService, messageService) {
            $controller('CustomerController');

            $scope.customers = [
                {"id": 1, "firstName": "Foo", "lastName": "Bar"},
                {"id": 2, "firstName": "Jim", "lastName": "Sunny"},
                {"id": 3, "firstName": "Peter", "lastName": "Prone"},
                {"id": 4, "firstName": "Sam", "lastName": "Sully"}
            ];

            $scope.save(2);

            customerService.saveCustomer.$resolve(true);

            expect(customerService.saveCustomer).toHaveBeenCalledWith({"id": 2, "firstName": "Jim", "lastName": "Sunny"});
            expect(messageService.error).not.toHaveBeenCalled();
        }));

    });
});