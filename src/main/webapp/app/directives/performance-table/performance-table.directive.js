(function() {
    'use strict';

    angular
        .module('funduniversApp')
        .directive('performanceTable', performanceTable);

    function performanceTable() {
        var directive = {
            restrict: 'E',
            scope: {
                data: '=data'
            },
            templateUrl: "/app/directives/performance-table/performance-table.html",
            link: linkFunc
        };

        return directive;

        function linkFunc(scope, element, attrs) {

            // transform data (array of objects => array of arrays)
            var formattedData = _(scope.data).map(function(item){return {date:item.date, value: item.value*100}}).value();
            scope.data = formattedData;
        }
    }
})();
