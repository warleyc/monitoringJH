'use strict';

angular.module('monitoringApp')
    .controller('ResponseDetailController', function ($scope, $rootScope, $stateParams, entity, Response, ProjectConfiguration) {
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
