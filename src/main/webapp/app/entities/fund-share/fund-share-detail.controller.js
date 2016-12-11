(function() {
    'use strict';

    angular
        .module('funduniversApp')
        .controller('FundShareDetailController', FundShareDetailController);

    FundShareDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FundShare'];

    function FundShareDetailController($scope, $rootScope, $stateParams, previousState, entity, FundShare) {
        var vm = this;

        vm.fundShare = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('funduniversApp:fundShareUpdate', function(event, result) {
            vm.fundShare = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
