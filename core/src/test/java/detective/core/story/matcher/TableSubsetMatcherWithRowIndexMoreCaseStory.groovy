package detective.core.story.matcher

import static detective.core.Detective.*;

story() "Detective Subset matcher story with row index number more cases" {
  
  scenario "sub table has less columns should match" {
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
        rowNumber   |intColumn   | stringColumn   
        0           |1           | "row1"         
        1           |4           | "row2"         
        2           |10          | "row3"         
     }
    }

    then "expectedTable is a subset of actual table"{
      expectedTable << subsetOf(actualTable)
    }
  }
  
  scenario "subset table row number should greater than 0" {
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
        -1           |4           | "row2"         | "row2 column2"
     }
    }

    then "expectedTable is a subset of actual table"{
      expect( "number have to between 0 and 2"){
        expectedTable << subsetOf(actualTable)
      }
    }
  }
  
  scenario "subset table row number should less than full set table count" {
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
        3           |4           | "row2"         | "row2 column2"
     }
    }

    then "expectedTable is a subset of actual table"{
      expect( "number have to between 0 and 2"){
        expectedTable << subsetOf(actualTable)
      }
    }
  }
  
  scenario "subset table has more columns should return an error" {
    given "table for actual value" {
      actualTable = table {
        intColumn   | stringColumn   | stringColumn2
        1           | "row1"         | "row1 column2"
     }
    }
    
    given "table for expected value" {
      expectedTable = table {
        rowNumber   |intColumn   | stringColumn   | stringColumn2   |additionalColumn
        0           |1           | "row1"         | "row1 column2"  | ""
      }
    }

    then "should raise an error say that can't found property additionalColumn"{
      expect ("No such property: additionalColumn for class: detective.core.dsl.table.Row"){
        expectedTable << subsetOf(actualTable)
      }      
    }
  }
}

