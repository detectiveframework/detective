package detective.core.example

import static detective.core.Detective.*;
import static detective.core.example.DetectiveExample.*;

story() "Detective Number Range Story" {
	scenario "A provided number is not within the specified range" {
		give "a number"{ number = "17" }
		give "a begin value"{ begin = "18"; }
		give "a end value"{ end = "50"; }

		when "run inRangeValidatinoTask task"{ runtask inRangeValidatinoTask() }

		then "The value is NOT within the provided range"{ inRange << false }
	}
	
	scenario "The incorrect value is provided" {
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
	
	scenario "A provided number is within the specified range" {
		give "a number"{ number = "25" }
		give "a begin value"{ begin = "18"; }
		give "a end value"{ end = "50"; }

		when "run inRangeValidatinoTask task"{ runtask inRangeValidatinoTask() }

		then "The value IS within the provided range"{ inRange << true }
	}
}



