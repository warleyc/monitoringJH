'use strict';

angular.module('monitoringApp')
    .controller('ProjectConfigurationDetailController', function ($scope, $rootScope, $stateParams, entity, ProjectConfiguration) {
        $scope.projectConfiguration = entity;
        $scope.load = function (id) {
            ProjectConfiguration.get({id: id}, function(result) {
                $scope.projectConfiguration = result;
            });
        };
        $rootScope.$on('monitoringApp:projectConfigurationUpdate', function(event, result) {
            $scope.projectConfiguration = result;
        });
    });
