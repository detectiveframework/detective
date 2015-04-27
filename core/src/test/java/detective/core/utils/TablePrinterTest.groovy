package detective.core.utils;

import static detective.core.Detective.*;
import detective.core.dsl.ParametersImpl
import detective.core.dsl.table.Row
import detective.utils.TablePrinter;
import static org.junit.Assert.*;
import detective.core.dsl.table.TableParser;

import org.junit.Test;

public class TablePrinterTest {

  @Test
  public void testPrintJSON() {
    def json = jsonBuilder().call([
      [
        name : "Guillaume",
        "age" : 33,
        pets : ["dog", "cat"]
      ],
      [
        name : "J",
        age : 11,
        pets : ["dog", "cat"]
      ]
      ])
    
    String printStr = TablePrinter.printJSON(json, "test")
    println printStr;
    
    assertEquals(printStr, 
"""
|================= test =================|
|  "name"       |  "age"  |  "pets"      |
|========================================|
|  "Guillaume"  |  33     |  [dog, cat]  |
|----------------------------------------|
|  "J"          |  11     |  [dog, cat]  |
|----------------------------------------|
""".toString()
      );    
  }
  
  @Test
  public void testPrintTable(){
    List<Row> rows = TableParser.asListOfRows(new ParametersImpl()) {
      fistname     |  lastname      |   job
      "Christian"  |  "Baranowski"  |  "Developer"
      "James"      |  "Bond"        |  "Agent"
    }
    
    assert rows.size() == 2;
    
    String printStr = TablePrinter.printTable(rows, "title");
    println printStr;
    
    assertEquals(printStr, 
"""
|==================== title =====================|
|  "fistname"   |  "lastname"    |  "job"        |
|================================================|
|  "Christian"  |  "Baranowski"  |  "Developer"  |
|------------------------------------------------|
|  "James"      |  "Bond"        |  "Agent"      |
|------------------------------------------------|
""".toString()
      );
  }

}
