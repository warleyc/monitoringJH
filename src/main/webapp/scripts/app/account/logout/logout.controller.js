'use strict';

angular.module('monitoringApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
