'use strict';

angular.module('monitoringApp')
    .factory('ProjectConfiguration', function ($resource) {
        return $resource('api/projectConfigurations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    if (data.lastError != null) data.lastError = new Date(data.lastError);
                    if (data.lastSucces != null) data.lastSucces = new Date(data.lastSucces);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
