package detective.core.story.tree

import static detective.core.Detective.*;
import static detective.core.Matchers.*;

import detective.core.TestTaskFactory;

story() "Share data control story" {
  desc """
    How a child story effect the shared data in parent?
    What happened if child story declare the same parameter?
    Don't allow? Overwrite? Ignore?
  
    Made decision, I would choose Don't allow.
    Overwrite and ignore is too easy to make mistake and confuse people.
    Share data is the key for parallel tasks, also easy to cause whole thread paused,
    don't want introduce more complexity into this system.
  """
  
  share {
    shared.toplevel.novalue
    shared.toplevel.valueassigned = "ChildLevel Assigned Value"
  }
  
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
