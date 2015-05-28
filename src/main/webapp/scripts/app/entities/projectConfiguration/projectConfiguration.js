'use strict';

angular.module('monitoringApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('projectConfiguration', {
                parent: 'entity',
                url: '/projectConfiguration',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'monitoringApp.projectConfiguration.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/projectConfiguration/projectConfigurations.html',
                        controller: 'ProjectConfigurationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('projectConfiguration');
                        return $translate.refresh();
                    }]
                }
            })
            .state('projectConfigurationDetail', {
                parent: 'entity',
                url: '/projectConfiguration/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'monitoringApp.projectConfiguration.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/projectConfiguration/projectConfiguration-detail.html',
                        controller: 'ProjectConfigurationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('projectConfiguration');
                        return $translate.refresh();
                    }]
                }
            });
    });
