'use strict';

angular.module('monitoringApp').controller('ResponseDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Response', 'ProjectConfiguration',
        function($scope, $stateParams, $modalInstance, entity, Response, ProjectConfiguration) {

        $scope.response = entity;
        $scope.projectconfigurations = ProjectConfiguration.query();
        $scope.load = function(id) {
            Response.get({id : id}, function(result) {
                $scope.response = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('monitoringApp:responseUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.response.id != null) {
                Response.update($scope.response, onSaveFinished);
            } else {
                Response.save($scope.response, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
