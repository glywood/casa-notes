var casaNotesApp = angular.module('casaNotesApp', ['ui.router', 'ngResource'])
.config(function($stateProvider, $urlRouterProvider) {

  // For any unmatched url, redirect to /chooser
  $urlRouterProvider.otherwise("/chooser");

  // Now set up the states
  $stateProvider
  .state('chooser', {
    url: "/chooser",
    views: {
      "personHeader": { template: "" },
      "mainView": {
        templateUrl: "chooser.html",
        controller: function($scope) {
          $scope.people = [{"name":"Geoff Lywood", "id":1}];
        }
      }
    }
  })
  .state('person', {
    abstract: true,
    url: "/person/{personId:int}",
    views: {
      "personHeader": { templateUrl: "person_header.html" },
      "mainView": { template: "<div ui-view></div>" }
    }
  })
  .state('person.overview', {
    url: "/overview",
    templateUrl: "overview.html",
    controller: 'OverviewController'
  })
  .state('person.activities', {
    url: "/activities",
    templateUrl: "activities.html",
    controller: 'ActivitiesController'
  })
  .state('person.activity', {
    url: "/activity/{activityId}",
    templateUrl: "activity.html",
    controller: 'ActivityController'
  })
  .state('person.reports', {
    url: "/reports",
    templateUrl: "reports.html"
  })
  .state('person.timeline', {
    url: "/timeline",
    templateUrl: "timeline.html"
  })
  .state('person.contacts', {
    url: "/contacts",
    templateUrl: "contacts.html"
  })
  .state('settings', {
    url: "/settings",
    views: {
      "personHeader": { template: "" },
      "mainView": { templateUrl: "settings.html" }
    }
  });
})

.controller('OverviewController', ['$scope', '$stateParams', function($scope, $stateParams) {
  $scope.personId = $stateParams.personId;
}])

.controller('ActivitiesController', ['$scope', '$stateParams', '$resource',
    function($scope, $stateParams, $resource) {

  var ActivitiesResource = $resource("/api/people/:personId/activities",
      {personId: $stateParams.personId})

  $scope.loading = true
  $scope.activities = []
  ActivitiesResource.query(function(activities) {
    $scope.activities = activities
    $scope.loading = false
  }, function(response) {
    $scope.error = response.data
    $scope.loading = false
  })
}])

.controller('ActivityController', ['$scope', '$stateParams', '$state', '$resource',
    function($scope, $stateParams, $state, $resource) {

  var ActivityResource = $resource("/api/people/:personId/activities/:id",
      {personId: $stateParams.personId})

  if ($stateParams.activityId !== 'new') {
    $scope.loading = true
    ActivityResource.get({id: $stateParams.activityId}, function(activity) {
      $scope.activity = activity
      $scope.loading = false
    }, function(response) {
      $scope.error = response.data
      $scope.loading = false
    })
  } else {
    $scope.activity = {
      summary: '',
      successes: '',
      concerns: ''
    }
  }

  $scope.save = function() {
    ActivityResource.save($scope.activity, function() {
      $state.go("person.activities")
    }, function(response) {
      $scope.error = response.data
    })
  }

  $scope.delete = function() {
    ActivityResource.delete({id: $stateParams.activityId}, function(activity) {
      $state.go("person.activities")
    }, function(response) {
      $scope.error = response.data
    })
  }
}])
