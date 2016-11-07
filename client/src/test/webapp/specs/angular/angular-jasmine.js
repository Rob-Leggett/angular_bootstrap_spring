'use strict';

angular.module('ngJasmine', []).service('$jasmine', function $jasmine($q, $rootScope) {
    function createPromise(spy) {
        var deferred = $q.defer();

        deferred.promise.$spy = spy;

        spy.$resolve = function $resolve(data) {
            deferred.resolve(data);

            $rootScope.$apply();
        };

        spy.$resolve.$alias = function $resolveAlias(name, aliasFn) {
            spy.$promise[name] = function (promiseFn) {
                spy.$promise.then(function (data) {
                    aliasFn(data, promiseFn);
                });

                return spy.$promise;
            };
        };

        spy.$resolve.$action = function $resolveAction() {
            Array.prototype.forEach.call(arguments, function (name) {
                spy.$resolve.$alias(name, function (data, promiseFn) {
                    if (data === name) {
                        promiseFn();
                    }
                });
            });
        };

        spy.$reject = function $reject(error) {
            deferred.reject(error);

            $rootScope.$apply();
        };

        spy.$reject.$alias = function $rejectAlias(name, aliasFn) {
            spy.$promise[name] = function (promiseFn) {
                spy.$promise.catch(function (error) {
                    aliasFn(error, promiseFn);
                });

                return spy.$promise;
            };
        };

        spy.$reject.$action = function $rejectAction() {
            Array.prototype.forEach.call(arguments, function (name) {
                spy.$reject.$alias(name, function (error, promiseFn) {
                    if (error === name) {
                        promiseFn();
                    }
                });
            });
        };

        return spy.and.returnValue(spy.$promise = deferred.promise);
    }

    this.createPromiseSpy = function createPromiseSpy(name) {
        return createPromise(jasmine.createSpy(name));
    };

    this.createPromiseSpyObj = function createPromiseSpyObj(baseName, methodNames) {
        var spyObj = jasmine.createSpyObj(baseName, methodNames);

        methodNames.forEach(function (methodName) {
            createPromise(spyObj[methodName]);
        });

        return spyObj;
    };
});

beforeEach(module('ngJasmine'));