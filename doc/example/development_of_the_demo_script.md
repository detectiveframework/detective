# Development of the Demo script

Below demo script was developed by the developer to aid the testing / BA team that suppose to perform the tests.


```groovy

story() "Detective Number Range Story demo" {

  scenario "A provided number is not within the specified range" {
    give "a number"{ number = "17" }
    give "a begin value"{ begin = "18"; }
    give "a end value"{ end = "50"; }

    when "run inRangeValidatinoTask task"{ runtask inRangeValidatinoTask() }

    then "The value is NOT within the provided range"{ inRange << false }
  }


```
#### Developer should also update the task reference documentation with more verbose details on the available functions and different type features they provide that will allow the testers to perform comprehensive testing.

