package detective.core.story.table

import static detective.core.Detective.*;

story() "More table scenarios" {
  
  scenario "TODO This haven't emplemented yet - header shouldn't effect by any variable" {
    given "a variable and a table with same head name" {
      col1 = 10
      mytable = table {
        col1    | col2    | expected
        1       | 2.0     | 3.0
       }
      
      runtask echoTask()
    }
    
    then "I can read my table a lot of ways"{
      mytable.each { row ->
        //row.expected << row.col1 + row.col2;
      }
//      mytable[0].col1 << 1
//      mytable[0].col2 << 2.0
//      col1 << 10
    }
  }
  
  scenario "int and float columns" {
    given "a table with col1, col2, and expected" {
      mytable = table {
        col1    | col2    | expected
        1       | 2.0     | 3.0
       }
      
      runtask echoTask()
    }
    
    then "I can read my table a lot of ways"{
      mytable.each { row ->
        //row.expected << row.col1 + row.col2;
      }
    }
  }
}

