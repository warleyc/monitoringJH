'use strict';

angular.module('monitoringApp')
    .factory('ProjectConfigurationLaunch', function ($rootScope, $http) {
        return {
            launch: function (id,scope) {
                return $http.get('api/projectConfigurations/launch/'+id.id).then(function (response) {

                    console.log("nicolas code : "+response.data.code);
                    console.log("nicolas message : "+response.data.responseMessage);
                    scope.response = response;
                    $('#launchResultProjectConfiguration').modal('show');
                    return response.data;
                });
            }
        };
    });
