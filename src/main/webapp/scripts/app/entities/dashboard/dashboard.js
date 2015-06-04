'use strict';

angular.module('monitoringApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dashboard', {
                parent: 'entity',
                url: '/dashboard',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'monitoringApp.dashboard.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dashboard/dashboards.html',
                        controller: 'DashboardController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dashboard');
                        $translatePartialLoader.addPart('projectConfiguration');
                        $translatePartialLoader.addPart('response');
                        return $translate.refresh();
                    }]
                }
            });
    });
