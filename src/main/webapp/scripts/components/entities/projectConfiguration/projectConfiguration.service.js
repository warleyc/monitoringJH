'use strict';

angular.module('monitoringApp')
    .factory('ProjectConfiguration', function ($resource, DateUtils) {
        return $resource('api/projectConfigurations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.lastError = DateUtils.convertDateTimeFromServer(data.lastError);
                    data.lastSucces = DateUtils.convertDateTimeFromServer(data.lastSucces);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
