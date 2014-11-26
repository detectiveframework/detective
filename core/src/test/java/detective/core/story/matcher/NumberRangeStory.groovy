package detective.core.story.matcher

import static detective.core.Detective.*;


story() "Detective Number Range Story" {
	scenario "A provided number should be within the specified range" {
		give "a number"{ number = "19" }
		give "a begin value"{ begin = "18"; }
		give "a end value"{ end = "50"; }

		when "run inRangeValidatinoTask task"{ runtask inRangeValidatinoTask() }

		then "should be within the provided range"{ inRange << true }
	}
}



