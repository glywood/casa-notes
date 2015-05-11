var casaNotesApp = angular.module('casaNotesApp', ['ui.router'])
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
    templateUrl: "activities.html"
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

.controller('ActivityController', ['$scope', '$stateParams', '$state', function($scope, $stateParams, $state) {
  $scope.save = function() {
    console.log("would save it here");
    $state.go("person.activities")
  };
}])