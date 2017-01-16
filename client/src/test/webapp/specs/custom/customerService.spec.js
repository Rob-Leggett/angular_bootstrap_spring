describe('CustomerService Tests', function (){

    beforeEach(module('app.services'));
    beforeEach(module(function ($provide) {
            $provide.value('$scope', {
                getCustomers: jasmine.createSpy('$scope.getCustomers'),
                deleteCustomer: jasmine.createSpy('$scope.deleteCustomer'),
                saveCustomer: jasmine.createSpy('$scope.saveCustomer')
            });
        })
    );

    describe('CustomerService Structural Tests', function() {

        it('should have an get customers function', inject(function ($scope) {
            expect(angular.isFunction($scope.getCustomers)).toBe(true);
        }));

        it('should have an delete customer function', inject(function ($scope) {
            expect(angular.isFunction($scope.deleteCustomer)).toBe(true);
        }));

        it('should have an save customer function', inject(function ($scope) {
            expect(angular.isFunction($scope.saveCustomer)).toBe(true);
        }));
    });

    describe('CustomerService Get Customers Tests', function() {
        it('should return results from get customers function call', inject(function ($httpBackend, customerService, propertiesConstant) {
            var customers = [
                {"id": 1, "firstName": "Foo", "lastName": "Bar"},
                {"id": 2, "firstName": "Jim", "lastName": "Sunny"},
                {"id": 3, "firstName": "Peter", "lastName": "Prone"},
                {"id": 4, "firstName": "Sam", "lastName": "Sully"}
            ];

            $httpBackend.whenGET(propertiesConstant.API_URL + '/customer').respond(customers);

            // check result returned from service call
            customerService.getCustomers().then(function (customers) {
                expect(customers).toEqual(customers);
            });

            $httpBackend.flush();

            $httpBackend.expectGET(propertiesConstant.API_URL + '/customer').respond(customers);
        }));
    });
});