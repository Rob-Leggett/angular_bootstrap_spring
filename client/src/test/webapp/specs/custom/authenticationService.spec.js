describe('AuthenticationService Tests', function (){
    var authenticationService;

    // excuted before each "it" is run.
    beforeEach(function (){

        // load the module.
        module('app.services');

        // inject your service for testing.
        // The _underscores_ are a convenience thing
        // so you can have your variable name be the
        // same as your injected service.
        inject(function(_authenticationService_) {
            authenticationService = _authenticationService_;
        });
    });

    it('should have a login function', function () {
        expect(angular.isFunction(authenticationService.login)).toBe(true);
    });

    it('should have a logout function', function () {
        expect(angular.isFunction(authenticationService.logout)).toBe(true);
    });
});