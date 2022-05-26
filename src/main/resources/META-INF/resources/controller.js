var app = angular.module("SearchModule", []);

//Controller Part
var controller = app.controller('SearchController', ['$scope', '$http', function($scope, $http) {
    //Initialize page with default data which is blank in this example
    $scope.products;
    $scope.name="";
    $scope.description="";

    $scope.findProducts = function(event){
        if($scope.name.length >= 4 || $scope.description.length >=4){
            _findProducts();
        }
    }

    //HTTP GET- get list of products
    function _findProducts() {
        let url = '/products/search/?'
        if($scope.name.length != ""){
            url=url+'name='+$scope.name;
        }
        if($scope.name.length != "" && $scope.description.length != ""){
            url=url+'&';
        }
        if($scope.description.length != ""){
            url=url+'description='+$scope.description;
        }

        $http({
            method: 'GET',
            url: url
        }).then(function successCallback(response) {
            $scope.products = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

}]);
