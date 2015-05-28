'use strict';

angular.module('monitoringApp')
    .factory('Response', function ($resource) {
        return $resource('api/responses/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    if (data.date != null) data.date = new Date(data.date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
