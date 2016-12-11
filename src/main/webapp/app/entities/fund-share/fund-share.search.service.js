(function() {
    'use strict';

    angular
        .module('funduniversApp')
        .factory('FundShareSearch', FundShareSearch);

    FundShareSearch.$inject = ['$resource'];

    function FundShareSearch($resource) {
        var resourceUrl =  'api/_search/fund-shares/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
