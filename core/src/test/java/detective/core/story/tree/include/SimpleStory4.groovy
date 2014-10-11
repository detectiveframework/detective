package detective.core.story.tree.include

import static detective.core.Detective.*;
import static detective.core.Matchers.*;

import detective.core.TestTaskFactory;


story() "Simple story" {
  scenario "Simple Scenario" {
    given "parameterA" {
      parameterA = 0
    }
    
    then "I should have parameterA echo back"{
      echo.parameterA << parameterA
      echo.parameterA << 0
    }
  }
}
