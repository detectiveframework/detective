package detective.core.distribute.collect

import static detective.core.Detective.*;
import static detective.core.Matchers.*;

import detective.core.TestTaskFactory;

import detective.core.distribute.ScenarioThreadRecorder;

story() "Story_1_Scenario_1" {
  desc """
    Just a simple story
  """
  
  scenario "Scenario1" {
    given "parameterA" {
      parameterA = 0
    }
    
    when "run echo task"{
      runtask echoTask()
    }
    
    then "I should have parameterA echo back"{
      echotask.parameterA << parameterA
      echotask.parameterA << 0
      
      ScenarioThreadRecorder.recordThread("sparkTest", "Story_1_Scenario_1 - Scenario1")
    }
  }
}
