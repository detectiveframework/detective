# Table to Table with code identifier

```groovy
package detective.core.story.matcher

import static detective.core.Detective.*;

story() "Detective Subset matcher story with row index number" {

  scenario "two table equals to each other, subset should still matched" {
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
        rowToMatch                                 |intColumn   | stringColumn   | stringColumn2
        row = {it.stringColumn == "row1"}          |1           | "row1"         | "row1 column2"
        row = {it.stringColumn2 == "row2 column2"} |4           | "row2"         | "row2 column2"
        row = {it.stringColumn == "row3"}          |10          | "row3"         | "row2 column2"
     }
    }

    then "expectedTable is a subset of actual table"{
      expectedTable << subsetOf(actualTable)
    }
  }

  scenario "full table larger then subset table should matched" {
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
        rowToMatch                                  |intColumn   | stringColumn   | stringColumn2
        test = {it.stringColumn2 == "row2 column2"} |4           | "row2"         | "row2 column2"
     }
    }

    then "expectedTable is a subset of actual table"{
      expectedTable << subsetOf(actualTable)
    }
  }
}
```
