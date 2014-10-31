# JOSN and Table

```groovy
story() "Detective Subset matcher story between Josn / List and table" {

  scenario "Json and table should able to compare" {
    given "json data for full set table" {
      fullsetJson = jsonParser("""
        [
          {"intColumn":1,  "stringColumn":"row1", "stringColumn2":"row1 column2"},
          {"intColumn":4,  "stringColumn":"row2", "stringColumn2":"row2 column2"},
          {"intColumn":10, "stringColumn":"row3", "stringColumn2":"row2 column2"}
        ]
      """)
    }

    given "table for expected value" {
      subsetTable = table {
        rowNumber   |intColumn   | stringColumn   | stringColumn2
        0           |1           | "row1"         | "row1 column2"
        1           |4           | "row2"         | "row2 column2"
        2           |10          | "row3"         | "row2 column2"
     }
    }

    then "subsetTable is a subset of fullsetJson"{
      subsetTable << subsetOf(fullsetJson)
    }
  }
}
```
