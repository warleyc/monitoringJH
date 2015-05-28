/* globals $ */
'use strict';

angular.module('monitoringApp')
    .directive('monitoringAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
