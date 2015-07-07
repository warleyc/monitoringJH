'use strict';

angular.module('monitoringApp')
    .controller('ProjectConfigurationController', function ($scope, ProjectConfiguration,ProjectConfigurationLaunch, ParseLinks) {
        $scope.projectConfigurations = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            ProjectConfiguration.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.projectConfigurations = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            ProjectConfiguration.get({id: id}, function(result) {
                $scope.projectConfiguration = result;
                $('#saveProjectConfigurationModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.projectConfiguration.id != null) {
                ProjectConfiguration.update($scope.projectConfiguration,
                    function () {
                        $scope.refresh();
                    });
            } else {
                ProjectConfiguration.save($scope.projectConfiguration,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            ProjectConfiguration.get({id: id}, function(result) {
                $scope.projectConfiguration = result;
                $('#deleteProjectConfigurationConfirmation').modal('show');
            });
        };


        $scope.launch = function (id) {
            ProjectConfigurationLaunch.launch({id: id,scope: $scope}, function(result) {
                console.log("nicolas lala");
                $scope.response = result;
                $('#launchResultProjectConfiguration').modal('show');
            });
        };


        $scope.confirmDelete = function (id) {
            ProjectConfiguration.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteProjectConfigurationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveProjectConfigurationModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.projectConfiguration = {name: null, url: null, post: null, parametre: null, requestMethod: null, header: null, frequence: null, login: null, password2: null, checkMessage: null, lastError: null, lastSucces: null, actif: null, alertSMS: null, alertMail: null, email: null, contentType: null, soap: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
