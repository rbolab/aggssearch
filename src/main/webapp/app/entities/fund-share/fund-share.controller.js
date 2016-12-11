(function() {
    'use strict';



    angular
        .module('funduniversApp')
        .controller('FundShareController', FundShareController);

    FundShareController.$inject = ['$scope', '$state', 'FundShare', 'FundShareSearch'];

    function FundShareController ($scope, $state, FundShare, FundShareSearch) {
        var vm = this;

        vm.fundShares = [];
        vm.total;
        vm.currency_buckets;
        vm.investorType_buckets;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            FundShare.query(function(result) {
                vm.fundShares = result.hits.hits;
                vm.total = result.hits.total;
                vm.currency_buckets = result.aggregations.aggs_currency.buckets;
                vm.investorType_buckets = result.aggregations.nested_aggs.investorType.buckets;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FundShareSearch.query({query: vm.searchQuery}, function(result) {
                vm.fundShares = result;
            });
        }


        $scope.isTableOpen = function($event) {
            var tableSelected = angular.element($event.currentTarget).parents('tbody.header, .tbody.header');
            tableSelected.toggleClass('open').next('tbody.content, .tbody.content').toggle();
        }

        var columnDefs = [
            {headerName: "Make", field: "make"},
            {headerName: "Model", field: "model"}
        ];

        var rowData = [
            {make: "Toyota", model: "Celica"},
            {make: "Ford", model: "Mondeo"},
            {make: "Porsche", model: "Boxter"}
        ];

        $scope.gridOptions = {
            columnDefs: columnDefs,
            rowData: rowData
        };
    }
})();
