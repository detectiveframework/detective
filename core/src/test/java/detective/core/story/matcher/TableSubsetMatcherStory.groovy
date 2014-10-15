package detective.core.story.matcher

import static detective.core.Detective.*;

story() "Detective Subset matcher story" {
  """
    In order to simplify table to table, json to table, list<Map> to table comparasion, we built a subset matcher for them.
  """
  
  scenario "two table equals to each other, subset still matched" {
    given "table for actual value" {
      actualTable = table {
        intColumn   | stringColumn   | stringColumn2
        1           | "row1"         | "row1 column2"
        4           | "row2"         | "row2 column2"
        10          | "row3"         | "row2 column2"
     }
    }
    
    given "table for expected value" {
      expectedTable = table {
        rowNumber   |intColumn   | stringColumn   | stringColumn2
        0           |1           | "row1"         | "row1 column2"
        1           |4           | "row2"         | "row2 column2"
        2           |10          | "row3"         | "row2 column2"
     }
    }

    then "expectedTable is a subset of actual table"{
      expectedTable << subsetOf(actualTable)
    }
  }
}

