package detective.core.distribute.jobresult

import static detective.core.Detective.*;


story() "Story For Testing Collect Running Result from Different Threads / Machines" {
  scenario "Successed Scenario" {
    given "step1 log a user message" {
      logMessage "This is the message will display to end user"
      echotest = "echo test"
    }
    
    when "run echo task"{
      runtask echoTask();
      runtask new TaskAbleToPrintUserMessage();
    }
    
    then "I can check message which will echo back"{
      echotask._userMessages.size << 0  //will be clean
      echotask.echotest << "echo test"
    }
  }
  
  scenario "Failed Scenario" {
    given "step1 log a user message" {
      logMessage "This is the message will display to end user"
      echotest = "echo test"
    }
    
    when "through exception"{
      throw new RuntimeException("Exception happened")
    }
    
    then "There is no chance this step will been executed, but this step will show in console so that user know one step has been scaped"{
      echotask._userMessages.size << 0  //will be clean
      echotask.echotest << "echo test"
    }
  }
}