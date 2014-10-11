package detective.core.runner

import static detective.core.Detective.*;

System.setProperty("detective.runner.scenario.index", "1")

story() "Run scenario number 1 only" {
  """
    As a developer, I sometimes just want run 1 scenario, you can setup a system property and tell detective 
    which scenario you'd like to run.
    In this story scenario0 and scenario2 has a failure check but if you run the whole story, no exception raised
    as we ran scenario1 only
  """
  
  scenario0 "Scenario 0 which will be ignored" {
    onlystep "all in one step" {
      parameterA = "This is the value"
      runtask echoTask();
      echotask.parameterA << "This is the WRONG!!! value"
    }    
  }
  
  scenario1 "Scenario 1 which will run" {
    onlystep "all in one step" {
      parameterA = "This is the value"
      runtask echoTask();
      echotask.parameterA << "This is the value"
    } 
  }
  
  scenario2 "Scenario 2 which will be ignored" {
    onlystep "all in one step" {
      parameterA = "This is the value"
      runtask echoTask();
      echotask.parameterA << "This is the WRONG!!! value"
    }    
  }
}

