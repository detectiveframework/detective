package detective.core.story.matcher

import static detective.core.Detective.*;

story() "Detective Equals Matcher Story" {

  scenario "Json and table should able to compare" {
    given "give a null value" {
      parameter = null
      parameter << null
    }
  }
  
}

