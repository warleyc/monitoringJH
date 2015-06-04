'use strict';

angular.module('monitoringApp')
    .controller('DashboardController', function ($scope, Dashboard) {

        $scope.dashboards = [];
        $scope.loadAll = function() {
            Dashboard.query(function(result) {
               $scope.dashboards = result;
            });
        };

        $scope.loadAll();

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveDashboardModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.dashboard = {id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };


    });
