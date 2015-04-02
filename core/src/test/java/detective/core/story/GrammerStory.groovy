package detective.core.story

import static detective.core.Detective.*;


story() "Story Grammers" {
  scenario "Log Message into system" {
    given "invoke logMessage" {
      logMessage "This is the message I'd like to display to end user."
    }
    
    when "run echo task"{
      runtask echoTask();
    }
    
    then "user message is for step only, after a step user message will be empty"{
      echotask._userMessages.size << 0
    }
  }
}