package detective.core.distribute.collect

import static detective.core.Detective.*;
import static detective.core.Matchers.*;

import detective.core.TestTaskFactory;

story() "Story with one scenario and no shared data" {
  desc """
    Just a simple story
  """
  
  scenario "Simple Scenario" {
    given "parameterA" {
      parameterA = 0
    }
    
    when "run echo task"{
      runtask echoTask()
    }
    
    then "I should have parameterA echo back"{
      echotask.parameterA << parameterA
      echotask.parameterA << 0
    }
  }
}
