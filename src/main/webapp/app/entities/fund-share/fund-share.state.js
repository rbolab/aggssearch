(function() {
    'use strict';

    angular
        .module('funduniversApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('fund-share', {
            parent: 'entity',
            url: '/fund-share',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FundShares'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fund-share/fund-shares.html',
                    controller: 'FundShareController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('fund-share-detail', {
            parent: 'entity',
            url: '/fund-share/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FundShare'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fund-share/fund-share-detail.html',
                    controller: 'FundShareDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FundShare', function($stateParams, FundShare) {
                    return FundShare.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'fund-share',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('fund-share-detail.edit', {
            parent: 'fund-share-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fund-share/fund-share-dialog.html',
                    controller: 'FundShareDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FundShare', function(FundShare) {
                            return FundShare.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fund-share.new', {
            parent: 'fund-share',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fund-share/fund-share-dialog.html',
                    controller: 'FundShareDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('fund-share', null, { reload: 'fund-share' });
                }, function() {
                    $state.go('fund-share');
                });
            }]
        })
        .state('fund-share.edit', {
            parent: 'fund-share',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fund-share/fund-share-dialog.html',
                    controller: 'FundShareDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FundShare', function(FundShare) {
                            return FundShare.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fund-share', null, { reload: 'fund-share' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fund-share.delete', {
            parent: 'fund-share',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fund-share/fund-share-delete-dialog.html',
                    controller: 'FundShareDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FundShare', function(FundShare) {
                            return FundShare.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fund-share', null, { reload: 'fund-share' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
