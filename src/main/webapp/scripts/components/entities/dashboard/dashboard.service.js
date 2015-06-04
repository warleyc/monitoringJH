'use strict';

angular.module('monitoringApp')
    .factory('Dashboard', function ($resource, DateUtils) {
        return $resource('api/dashboards/:id', {}, {
            'query': { method: 'GET', isArray: false},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
