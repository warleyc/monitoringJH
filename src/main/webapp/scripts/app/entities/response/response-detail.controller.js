'use strict';

angular.module('monitoringApp')
    .controller('ResponseDetailController', function ($scope, $stateParams, Response) {
        $scope.response = {};
        $scope.load = function (id) {
            Response.get({id: id}, function(result) {
              $scope.response = result;
            });
        };
        $scope.load($stateParams.id);
    });
