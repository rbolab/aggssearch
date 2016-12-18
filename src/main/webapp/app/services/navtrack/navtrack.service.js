(function () {
    'use strict';

    angular
        .module('funduniversApp')
        .factory('NavTrack', NavTrack);

    NavTrack.$inject = ['$resource'];

    function NavTrack ($resource) {
        return $resource('/app/data/navtrack.json');
    }
})();
