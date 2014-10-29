package detective.core.story.matcher

import static detective.core.Detective.*;

story() "Detective Subset matchers between List<Map> and Table" {
  
  scenario "List<Map<String, Object>> and table should able to compare" {
    given "List<Map<String, Object>> for full set table" {
      fullsetList = [
        [intColumn:1,     stringColumn:"row1",    stringColumn2:"row1 column2"],
        ["intColumn":4,   "stringColumn":"row2",  "stringColumn2":"row2 column2"],
        ["intColumn":10,  "stringColumn":"row3", "stringColumn2":"row2 column2"]
      ]      
    }
    
    given "table for expected value" {
      subsetTable = table {
        rowNumber   |intColumn   | stringColumn   | stringColumn2
        0           |1           | "row1"         | "row1 column2"
        1           |4           | "row2"         | "row2 column2"
        2           |10          | "row3"         | "row2 column2"
     }
    }

    then "should match and no errors"{
      subsetTable << subsetOf(fullsetList)
    }
  }
  
}

