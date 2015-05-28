'use strict';

angular.module('monitoringApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('response', {
                parent: 'entity',
                url: '/response',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'monitoringApp.response.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/response/responses.html',
                        controller: 'ResponseController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('response');
                        return $translate.refresh();
                    }]
                }
            })
            .state('responseDetail', {
                parent: 'entity',
                url: '/response/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'monitoringApp.response.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/response/response-detail.html',
                        controller: 'ResponseDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('response');
                        return $translate.refresh();
                    }]
                }
            });
    });
