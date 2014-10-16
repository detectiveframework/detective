package detective.core.story

import static detective.core.Detective.*;

System.setProperty("detective.runner.scenario.index", "1");

story() "Demo for tables" {
  """
    In order to organize data easier, we provide scenario table and normal table.
  
  """
  
  scenario "Scenario Table" {
    scenarioTable {
      given2.col1 | given2.col2 | given2.expected
      1           | 2           | 3
      4           | 5           | 9
      10          | 11          | 21
     }
    
    given "echo 1" {
      parameter.given1 = "given1"
      runtask echoTask()
    }
    
    given "echo 2"{
      parameter.given2 = "given2"
      runtask echoTask()
    }

    then "It will run echo twice and the output of first task will become input of second task"{
      echotask.parameter.given1 << "given1"
      echotask.echotask.parameter.given1 << "given1"
      echotask.parameter.given2 << "given2"
      
      echotask.given2.expected  << (echotask.given2.col1 + echotask.given2.col2)
    }
  }
  
  scenario "Scenario Table two rows only" {
    scenarioTable {
      given2.col1 | given2.col2 | given2.expected
      1           | 2           | 3
     }
    
    given "echo 1" {
      parameter.given1 = "given1"
      runtask echoTask()
    }
    
    given "echo 2"{
      parameter.given2 = "given2"
      runtask echoTask()
    }

    then "It will run echo twice and the output of first task will become input of second task"{
      echotask.parameter.given1 << "given1"
      echotask.echotask.parameter.given1 << "given1"
      echotask.parameter.given2 << "given2"
      
      echotask.given2.expected  << (echotask.given2.col1 + echotask.given2.col2)
    }
  }
  
  scenario "Normal Data Table" {
    given "a table with col1, col2, and expected" {
      mytable = table {
        col1    | col2    | expected
        1       | 2       | 3
        4       | 5       | 9
        10      | 11      | 21
       }
      
      runtask echoTask()
    }
    
    then "I can read my table a lot of ways"{
      mytable.each { row ->
        row.expected << row.col1 + row.col2;
      }
    }
  }
  
  scenario "Normal Data Table two rows only" {
    given "a table with col1, col2, and expected" {
      mytable = table {
        col1    | col2    | expected
        1       | 2       | 3
       }
      
      runtask echoTask()
    }
    
    then "I can read my table a lot of ways"{
      mytable.each { row ->
        row.expected << row.col1 + row.col2;
      }
    }
  }
}

