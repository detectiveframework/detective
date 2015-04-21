package detective.core.story.output

import static detective.core.Detective.*;

story() "Demo for how story and task effect output" {
   
  scenario "A passed simple case" {
    given "a parameter out side of scenario table" {
      parameter.given1 = "given1"
      runtask echoTask()
    }
    
    then "log infomration out to console"{
      logMessage("This message will print out to console with bold style")
      exampleTable = table {
        rowIndex  |col1    | col2    | expected  | msg
        0         |1       | 2       | 3         | "msg1"
        1         |4       | 5       | 9         | "msg2"
       }
      logMessage("You can show table as well")
      logMessage(exampleTable)
    }
    
    then "It will run echo parameter.given1"{
      echotask.parameter.given1 << "given1"      
    }
  }
  
//  scenario "A passed case with scenario table" {
//    scenarioTable {
//      given2.col1 | given2.col2 | given2.expected
//      1           | 2           | 3
//      4           | 5           | 9
//      10          | 11          | 21
//     }
//    
//    given "a parameter out side of scenario table" {
//      parameter.given1 = "given1"
//      runtask echoTask()
//    }
//    
//    then "It will run echo twice and the output of first task will become input of second task"{
//      echotask.parameter.given1 << "given1"
//
//      echotask.given2.expected  << (echotask.given2.col1 + echotask.given2.col2)
//    }
//  }
//  
  scenario "One table not a subset of other table" {
    given "a table with col1, col2, and expected" {
      myOriginTable = table {
        col1    | col2    | expected  | msg
        1       | 2       | 3         | "msg1"
        4       | 5       | 9         | "msg2-Wrong"
        10      | 11      | 21        | "msg3"
       }
      
      myExpectedTable = table {
        rowIndex  |col1    | col2    | expected  | msg
        0         |1       | 2       | 3         | "msg1"
        1         |4       | 5       | 9         | "msg2"
        2         |10      | 11      | 21        | "msg3"
       }
      
    }
    
    then "this scearnio will fail and console should print out two tables"{
      myExpectedTable << subsetOf(myOriginTable)
    }
  }
  
}

