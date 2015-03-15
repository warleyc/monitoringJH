'use strict';

angular.module('monitoringApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


