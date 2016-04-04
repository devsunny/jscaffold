'use strict';
/**
 * @ngdoc function
 * @name ${config.angularAppName}.controller:${entity.objectName}FormCtrl
 * @description
 * # ${entity.objectName}Ctrl
 * Controller of the ${config.angularAppName}
 */
angular.module('${config.angularAppName}', ['ui.codemirror'])
  .controller('${entity.objectName}FormCtrl', ['$scope', '$filter', '$interpolate', '$http', '$state', '$stateParams', 
  function ($scope, $filter, $interpolate, $http,  '$state', $stateParams) {
   $scope.${entity.varName}={};   
   $scope.message="";
   
   
  <#if entity.hasDatetimeField >
   
   /**
   * change field name if date field
   **/
   $scope.datepickers = {
        dateFieldName1: false,
        dateFieldName2: false
   }
   	$scope.openCalendar = function($event, which) {
	    $event.preventDefault();
	    $event.stopPropagation();	
	    $scope.datepickers[which]= true;
	};
	
	$scope.dateOptions = {
	    'year-format': "'yyyy'",
	    'starting-day': 1
	};  
	
	</#if>
      	
   	$scope.go=function(uri)
   	{   		
   		$state.go(uri);
   	}; 	
   	
   	   	
   	$scope.save=function()
   	{   		
   		$scope.message="";
   		$http.post('/${config.webappContext}/${entity.varName}', $scope.${entity.varName}).then(function(jsonEnvlope){
   			$scope.message="Sucessfully inserted"; 
   		},    		
   		function(jsonEnvlope){
   			$scope.message="Failed to add new record" ; 
   		});  	
   	};
   	
   	$scope.update=function()
   	{
   		$scope.message="";
   		$http.put('/${config.webappContext}/${entity.varName}', $scope.${entity.varName}).then(function(jsonEnvlope){
   			$scope.message="Sucessfully inserted"; 
   		},    		
   		function(jsonEnvlope){
   			$scope.message="Failed to add new record" ; 
   		});   		
   	};
   
   
   $scope.get=function()
   {   		     		 
	 
	 <#list entity.keyFields as field>	 
	 if (typeof($stateParams.${field.varName} == 'undefined' || $stateParams.${field.varName}==null){
	 	return;
	 }	 
	 </#list>	 
	 var urlExpr = $interpolate('/${config.webappContext}/${entity.varName}<#list entity.keyFields as field>/{{${field.varName}}}</#list>');
	 var url  = urlExpr($stateParams);   		 		 
	 $http.get(url).then(function(jsonEnvlope){ 
		$scope.${entity.varName}=jsonEnvlope.data ;    		
	 }, function(jsonEnvlope){
		$scope.message="Failed to fetch record" ; 
	 });  		
   };
   
}]);
