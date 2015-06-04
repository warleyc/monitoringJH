'use strict';

angular.module('monitoringApp')
    .factory('ProjectConfigurationLaunch', function ($rootScope, $http) {
        return {
            launch: function (id) {
                return $http.get('api/projectConfigurations/launch/'+id.id).then(function (response) {
                    return response.data;
                });
            }
        };
    });
