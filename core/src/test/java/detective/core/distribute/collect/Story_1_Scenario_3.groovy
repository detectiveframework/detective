package detective.core.distribute.collect

import static detective.core.Detective.*;
import static detective.core.Matchers.*;

import detective.core.TestTaskFactory;
import detective.core.distribute.ScenarioThreadRecorder;


story() "Story_1_Scenario_3" {
  desc """
    Just a simple story
  """
  
  scenario1 "Story_1_Scenario_3 Simple Scenario 1" {
    given "parameterA" {
      parameterA = 0
    }
    
    when "run echo task"{
      runtask echoTask()
    }
    
    then "I should have parameterA echo back"{
      echotask.parameterA << parameterA
      echotask.parameterA << 0
      ScenarioThreadRecorder.recordThread("sparkTest", "Story_1_Scenario_3 - Scenario1")
    }
  }
  
  scenario2 "Story_1_Scenario_3 Simple Scenario 2" {
    given "parameterA" {
      parameterA = 0
    }
    
    when "run echo task"{
      runtask echoTask()
    }
    
    then "I should have parameterA echo back"{
      echotask.parameterA << parameterA
      echotask.parameterA << 0
      
      ScenarioThreadRecorder.recordThread("sparkTest", "Story_1_Scenario_3 - Scenario2")
    }
  }
  
  scenario3 "Story_1_Scenario_3 Simple Scenario 3" {
    given "parameterA" {
      parameterA = 0
    }
    
    when "run echo task"{
      runtask echoTask()
    }
    
    then "I should have parameterA echo back"{
      echotask.parameterA << parameterA
      echotask.parameterA << 0
      
      ScenarioThreadRecorder.recordThread("sparkTest", "Story_1_Scenario_3 - Scenario3")
    }
  }
}
