'use strict';

angular.module('monitoringApp')
    .factory('ProjectConfigurationSearch', function ($resource) {
        return $resource('api/_search/projectConfigurations/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
