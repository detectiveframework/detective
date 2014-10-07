package detective.core.script

import static detective.core.Detective.*;


story() "Simple Story with Echo Task" {
  inOrderTo "demostrate simple story"
  
  scenario "Echo parameters back" {
    given "parameter a" {
      parameterA = "This is A"
    }
    
    when "run echo task"{
      runtask echoTask();
    }
    
    then "parameters will echo back"{
      echotask.parameterA << "This is A"
    }
    
    given "parameter b" {
      parameterB = "This is B"
    }
    
    when "run echo task"{
      runtask echoTask();
    }
    
    then "parameters will echo back"{
      echotask.parameterB << "This is B"
    }
  }
}

