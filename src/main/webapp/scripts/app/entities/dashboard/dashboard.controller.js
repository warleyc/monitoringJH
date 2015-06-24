'use strict';

angular.module('monitoringApp')
    .controller('DashboardController', function ($scope, Dashboard) {

        $scope.dashboards = [];
        $scope.loadAll = function() {
            Dashboard.query(function(result) {
               $scope.dashboards = result;
                $scope.loadChart();
            });
        };

        $scope.loadAll();



        $scope.loadChart = function () {

        $('#container').highcharts({
            title: {
                text: 'Monthly Average Error',
                x: -20 //center
            },
            subtitle: {
                text: 'Source: The database',
                x: -20
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },
            yAxis: {
                title: {
                    text: 'Total Error'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                valueSuffix: '°C'
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: $scope.dashboards.series
        });

        };


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
