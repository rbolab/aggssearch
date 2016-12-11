(function() {
    'use strict';
    angular
        .module('funduniversApp')
        .factory('FundShare', FundShare);

    FundShare.$inject = ['$resource'];

    function FundShare ($resource) {
        var resourceUrl =  'api/fund-shares/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: false},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
