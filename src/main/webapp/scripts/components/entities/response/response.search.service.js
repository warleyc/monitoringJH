'use strict';

angular.module('monitoringApp')
    .factory('ResponseSearch', function ($resource) {
        return $resource('api/_search/responses/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
