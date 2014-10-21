package detective.core.distribute.collect.subpackage

import static detective.core.Detective.*;
import static detective.core.Matchers.*;

import detective.core.TestTaskFactory;

import detective.core.distribute.ScenarioThreadRecorder;

story() "Story 1 with one scenario and no shared data" {
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
      ScenarioThreadRecorder.recordThread("sparkTest", "Story_2_Scenario_2_subpackage_FirstStory - Scenario1")
    }
  }
  
  scenario "Simple Scenario 2" {
    given "parameterA" {
      parameterA = 0
    }
    
    when "run echo task"{
      runtask echoTask()
    }
    
    then "I should have parameterA echo back"{
      echotask.parameterA << parameterA
      echotask.parameterA << 0
      ScenarioThreadRecorder.recordThread("sparkTest", "Story_2_Scenario_2_subpackage_FirstStory - Scenario2")
    }
  }
}


story() "Story 2 with 2 scenarios" {
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
      ScenarioThreadRecorder.recordThread("sparkTest", "Story_2_Scenario_2_subpackage_SecondStory - Scenario1")
    }
  }
  
  scenario "Simple Scenario 2" {
    given "parameterA" {
      parameterA = 0
    }
    
    when "run echo task"{
      runtask echoTask()
    }
    
    then "I should have parameterA echo back"{
      echotask.parameterA << parameterA
      echotask.parameterA << 0
      ScenarioThreadRecorder.recordThread("sparkTest", "Story_2_Scenario_2_subpackage_SecondStory - Scenario2")
    }
  }
}
