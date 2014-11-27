# Example

Below example will guide you through the steps to follow in order to write a test script covering both positive and negative cases.




```groovy
story() "Detective Number Range Story" {

  scenario "A provided number is not within the specified range" {
    give "a number"{ number = "17" }
    give "a begin value"{ begin = "18"; }
    give "a end value"{ end = "50"; }

    when "run inRangeValidatinoTask task"{ runtask inRangeValidatinoTask() }

    then "The value is NOT within the provided range"{ inRange << false }
  }

	scenario "A provided number is within the specified range" {
		give "a number"{ number = "25" }
		give "a begin value"{ begin = "18"; }
		give "a end value"{ end = "50"; }

		when "run inRangeValidatinoTask task"{ runtask inRangeValidatinoTask() }

		then "The value IS within the provided range"{ inRange << true }
	}

  scenario "This should fail with an exception since parameter is not provided" {
    give "a begin value"{
      begin = "18";
    }
    give "a end value"{
      end = "50";
    }

    then "run inRangeValidatinoTask task should return an error"{
      expect("The value to check against the range cannot be empty!"){
        runtask inRangeValidatinoTask()
      }
    }
  }
}```

