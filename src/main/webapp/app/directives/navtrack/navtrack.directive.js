(function() {
    'use strict';

    angular
        .module('funduniversApp')
        .directive('navTrack', navTrack);

    function navTrack() {
        var directive = {
            restrict: 'E',
            scope: {
                data: '=data'
            },
            templateUrl: "/app/directives/navtrack/navtrack.html",
            link: linkFunc
        };

        return directive;

        function linkFunc(scope, element, attrs) {

            // transform data (array of objects => array of arrays)
            var formattedData = _(scope.data).map(point => [new Date(point.date).getTime(), point.value]).value();

            Highcharts.stockChart('hightcharts-historical-chart', {
                chart: {
                    zoomType: 'x'
                },
                yAxis: {
                    opposite: false
                },
                tooltip: {
                    headerFormat: '<span>{point.key}</span><br/>',
                    pointFormat: '<span style="color:{point.color}">\u25CF</span> <span>{series.name}: {point.y}</span><br/>',
                    valueDecimals: 2
                },
                navigator: {
                    enabled: false
                },
                legend: {
                    enabled: false
                },
                series: [{
                    name: 'AAPL',
                    type: 'line',
                    data: formattedData
                }]
            });

        }
    }
})();
