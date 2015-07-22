'use strict';

angular.module('monitoringApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('projectConfiguration', {
                parent: 'entity',
                url: '/projectConfigurations',
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
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('response');
                        return $translate.refresh();
                    }]
                }
            })
            .state('projectConfiguration.detail', {
                parent: 'entity',
                url: '/projectConfiguration/{id}',
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
                    }],
                    entity: ['$stateParams', 'ProjectConfiguration', function($stateParams, ProjectConfiguration) {
                        return ProjectConfiguration.get({id : $stateParams.id});
                    }]
                }
            })
            .state('projectConfiguration.new', {
                parent: 'projectConfiguration',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/projectConfiguration/projectConfiguration-dialog.html',
                        controller: 'ProjectConfigurationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, url: null, post: null, parametre: null, requestMethod: null, header: null, frequence: null, login: null, password2: null, checkMessage: null, lastError: null, lastSucces: null, actif: null, alertSMS: null, alertMail: null, email: null, contentType: null, soap: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('projectConfiguration', null, { reload: true });
                    }, function() {
                        $state.go('projectConfiguration');
                    })
                }]
            })
            .state('projectConfiguration.edit', {
                parent: 'projectConfiguration',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/projectConfiguration/projectConfiguration-dialog.html',
                        controller: 'ProjectConfigurationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ProjectConfiguration', function(ProjectConfiguration) {
                                return ProjectConfiguration.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('projectConfiguration', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            }).state('projectConfiguration.launch', {
                    parent: 'projectConfiguration',
                    url: '/{id}/launch',
                    data: {
                        roles: ['ROLE_USER'],
                        pageTitle: 'monitoringApp.response.detail.title'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'scripts/app/entities/response/response-detail.html',
                            controller: 'ProjectConfigurationLaunch'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('response');
                            return $translate.refresh();
                        }],
                        entity: ['$stateParams', 'Response','$http', function($stateParams, ProjectConfiguration,$http) {
                            return $http.get('api/projectConfigurations/'+$stateParams.id+'/launch').then(function (response) {
                                return response.data;
                            });


                        }]
                    }
                });
    });
