'use strict';

angular.module('monitoringApp').controller('ProjectConfigurationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ProjectConfiguration',
        function($scope, $stateParams, $modalInstance, entity, ProjectConfiguration) {

        $scope.projectConfiguration = entity;
        $scope.load = function(id) {
            ProjectConfiguration.get({id : id}, function(result) {
                $scope.projectConfiguration = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('monitoringApp:projectConfigurationUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.projectConfiguration.id != null) {
                ProjectConfiguration.update($scope.projectConfiguration, onSaveFinished);
            } else {
                ProjectConfiguration.save($scope.projectConfiguration, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
