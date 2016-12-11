(function() {
    'use strict';

    angular
        .module('funduniversApp')
        .controller('FundShareDeleteController',FundShareDeleteController);

    FundShareDeleteController.$inject = ['$uibModalInstance', 'entity', 'FundShare'];

    function FundShareDeleteController($uibModalInstance, entity, FundShare) {
        var vm = this;

        vm.fundShare = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FundShare.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
