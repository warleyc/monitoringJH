'use strict';

angular.module('monitoringApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('response', {
                parent: 'entity',
                url: '/responses',
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
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('response.detail', {
                parent: 'entity',
                url: '/response/{id}',
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
                    }],
                    entity: ['$stateParams', 'Response', function($stateParams, Response) {
                        return Response.get({id : $stateParams.id});
                    }]
                }
            })
            .state('response.new', {
                parent: 'response',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/response/response-dialog.html',
                        controller: 'ResponseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {type: null, message: null, response: null, code: null, duration: null, configurationName: null, configurationId: null, emailSent: null, date: null, stackTrace: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('response', null, { reload: true });
                    }, function() {
                        $state.go('response');
                    })
                }]
            })
            .state('response.edit', {
                parent: 'response',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/response/response-dialog.html',
                        controller: 'ResponseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Response', function(Response) {
                                return Response.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('response', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
