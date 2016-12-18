(function() {
    'use strict';

    angular
        .module('funduniversApp')
        .controller('FundShareDetailController', FundShareDetailController);

    FundShareDetailController.$inject = ['$state', '$scope', '$rootScope', '$stateParams', 'previousState', 'navtrack', 'FundShare', 'NavTrack'];

    function FundShareDetailController($state, $scope, $rootScope, $stateParams, previousState, navtrack, FundShare, NavTrack) {
        var vm = this;

        vm.fundShare;
        vm.navtrack = navtrack;
        vm.previousState = previousState.name;
        //vm.$state = $state;

    }
})();
