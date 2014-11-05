package detective.core.story.table

import static detective.core.Detective.*;

story() "More table scenarios" {
  
  scenario "int and float columns" {
    given "a table with col1, col2, and expected" {
      mytable = table {
        col1    | col2    | expected
        1       | 2.0     | 3
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

