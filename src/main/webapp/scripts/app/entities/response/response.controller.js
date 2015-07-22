'use strict';

angular.module('monitoringApp')
    .controller('ResponseController', function ($scope, Response, ParseLinks) {
        $scope.responses = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Response.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.responses.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.responses = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Response.get({id: id}, function(result) {
                $scope.response = result;
                $('#deleteResponseConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Response.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteResponseConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.response = {type: null, message: null, response: null, code: null, duration: null, emailSent: null, date: null, stackTrace: null, id: null};
        };
    });
