package detective.core.script

import static detective.core.Detective.*;
import detective.task.EchoTask

story() "Simple Story with Echo Task" {
  inOrderTo "demostrate simple story"
  
  scenario "Echo parameters back" {
    given "a parameter" {
      parameterA = "This is the value"
    }
    
    when "run echo task"{
      runtask echoTask();
    }
    
    then "parameters will echo back"{
      echotask.parameterA << "This is the value"
    }
  }
  
  scenario "Echo parameters back with two tasks" {
    given "a parameter" {
      parameterA = "This is the value"
    }
    
    when "run echo task"{
      runtask twoEchoTasks()
    }
    
    then "parameters will echo back twice"{
      echotask.parameterA << "This is the value"
      echotask.echotask.parameterA << "This is the value"
    }
  }
}

def twoEchoTasks(){
  (1..2).collect {
    new EchoTask();
  }
}

