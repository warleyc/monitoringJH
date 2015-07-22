'use strict';

angular.module('monitoringApp')
    .controller('ProjectConfigurationLaunch', function ($scope, $rootScope, $stateParams, entity, Response) {

        console.log("nicolas code : "+entity);
        console.log("nicolas message : "+ Response);


        $scope.response = entity;
        $scope.load = function (id) {
           Response.get({id: id}, function(result) {
                $scope.response = result;
            });

        };
        $rootScope.$on('monitoringApp:responseUpdate', function(event, result) {
            $scope.response = result;
        });
    });

