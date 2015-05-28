'use strict';

angular.module('monitoringApp')
    .controller('ProjectConfigurationDetailController', function ($scope, $stateParams, ProjectConfiguration) {
        $scope.projectConfiguration = {};
        $scope.load = function (id) {
            ProjectConfiguration.get({id: id}, function(result) {
              $scope.projectConfiguration = result;
            });
        };
        $scope.load($stateParams.id);
    });
