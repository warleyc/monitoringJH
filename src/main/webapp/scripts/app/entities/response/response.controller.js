'use strict';

angular.module('monitoringApp')
    .controller('ResponseController', function ($scope, Response, ParseLinks) {
        $scope.responses = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Response.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.responses = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Response.get({id: id}, function(result) {
                $scope.response = result;
                $('#saveResponseModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.response.id != null) {
                Response.update($scope.response,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Response.save($scope.response,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Response.get({id: id}, function(result) {
                $scope.response = result;
                $('#deleteResponseConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Response.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteResponseConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveResponseModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.response = {type: null, message: null, response: null, code: null, duration: null, configurationName: null, configurationId: null, emailSent: null, date: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
