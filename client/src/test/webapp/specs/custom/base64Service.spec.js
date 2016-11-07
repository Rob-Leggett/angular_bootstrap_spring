describe('Base64Service Tests', function (){
    var base64Service;

    // excuted before each "it" is run.
    beforeEach(function (){

        // load the module.
        module('app.services');

        // inject your service for testing.
        // The _underscores_ are a convenience thing
        // so you can have your variable name be the
        // same as your injected service.
        inject(function(_base64Service_) {
            base64Service = _base64Service_;
        });
    });

    it('should have an encode function', function () {
        expect(angular.isFunction(base64Service.encode)).toBe(true);
    });

    it('should have an decode function', function () {
        expect(angular.isFunction(base64Service.decode)).toBe(true);
    });
});