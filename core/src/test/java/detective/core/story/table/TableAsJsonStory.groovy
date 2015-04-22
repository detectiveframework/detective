package detective.core.story.table

import static detective.core.Detective.*;

story() "Table map and json convert story" {
  
  scenario "convert table to a json" {
    given "a table with same head name" {
      mytable = table {
        col1    | col2    | expected | name
        1       | 2.0     | 3.0      | "abc"
        4       | 5.0     | 6.0      | "xyz"
       }
      
      mapList = mytable.collect{
        it.asMap()
      }
      mapList.size() << 2
      mapList[0].col1 << 1
      
      println mapList
      logMessage mapList
    }
    
  }
}

