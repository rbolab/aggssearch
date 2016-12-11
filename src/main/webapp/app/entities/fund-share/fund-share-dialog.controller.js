(function() {
    'use strict';

    angular
        .module('funduniversApp')
        .controller('FundShareDialogController', FundShareDialogController);

    FundShareDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FundShare'];

    function FundShareDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FundShare) {
        var vm = this;

        vm.fundShare = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.fundShare.id !== null) {
                FundShare.update(vm.fundShare, onSaveSuccess, onSaveError);
            } else {
                FundShare.save(vm.fundShare, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('funduniversApp:fundShareUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
