var casaNotesApp = angular.module('casaNotesApp', ['ui.router', 'ngResource'])
.config(function($stateProvider, $urlRouterProvider, $httpProvider) {

  // All HTTP requests should have the X-Requested-By header to prevent CSRF
  // This is the Jersey style of CSRF protection, not the Angular style
  $httpProvider.defaults.headers.common['X-Requested-By'] = "CASA Notes";

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
        controller: 'ChooserController'
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
    url: "/activity/{activityId}?type",
    templateUrl: "activity.html",
    controller: 'ActivityController'
  })
  .state('person.reports', {
    url: "/reports",
    templateUrl: "reports.html",
    controller: 'ReportsController'
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

.controller('ChooserController', ['$scope', '$stateParams', '$state', '$resource',
    function($scope, $stateParams, $state, $resource) {

  var PeopleResource = $resource("/api/people/:personId")

  $scope.loading = true
  PeopleResource.query(function(people) {
    $scope.people = people
    $scope.loading = false
  }, function(response) {
    $scope.error = response.data
    $scope.loading = false
  })

  $scope.addPerson = function() {
    $scope.loading = true
    PeopleResource.save($scope.toAdd, function(newPerson) {
      $scope.people.push(newPerson)
      $scope.loading = false
    }, function(response) {
      $scope.error = response.data
      $scope.loading = false
    })
  }
}])

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
    for (i = 0; i < activities.length; i++) {
      activities[i].formattedDuration = new Duration(activities[i].duration).value()
    }
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
      var duration = new Duration(activity.duration).value()
      $scope.activity = activity
      $scope.hours = duration.hours
      $scope.minutes = duration.minutes
      $scope.loading = false
    }, function(response) {
      $scope.error = response.data
      $scope.loading = false
    })
  } else {
    $scope.activity = {
      type: $stateParams.type,
      summary: '',
      successes: '',
      concerns: ''
    }
    $scope.hours = ""
    $scope.minutes = ""
  }

  $scope.save = function() {
    $scope.activity.duration = "PT" + Math.floor($scope.hours) + "H" + Math.floor($scope.minutes) + "M"
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

.controller('ReportsController', ['$scope', '$stateParams', '$resource',
    function($scope, $stateParams, $resource) {

  var ReportsResource = $resource("/api/people/:personId/reports")

  $scope.update = function() {
    $scope.report = undefined
    if (/\d\d\d\d-\d\d-\d\d/.test($scope.start)
        && /\d\d\d\d-\d\d-\d\d/.test($scope.end)) {
      ReportsResource.get({personId: $stateParams.personId, start: $scope.start, end: $scope.end},
      function(report) {
        $scope.report = report
        var dur = new Duration(report.duration).value();
        $scope.formattedDuration = dur.hours + "h " + dur.minutes + "m"
      }, function(response) {
        $scope.error = response.data
      })
    }
  }
}])
