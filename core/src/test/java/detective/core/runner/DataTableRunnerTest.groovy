package detective.core.runner;

import static org.junit.Assert.*;

import static detective.core.Detective.*;
import static detective.core.Matchers.*;

import detective.core.dsl.DslException
import detective.core.TestTaskFactory;

import detective.core.Story
import detective.core.StoryRunner
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataTableRunnerTest {
  
  
  
  @Before
  public void setUp() throws Exception {
    
  }

  @After
  public void tearDown() throws Exception {
    
  }
  
  StoryRunner runner = new SimpleStoryRunner();

  @Test
  public void test() {
    story() "Returns go to stock" {
      inOrderTo "keep track of stock"
      asa "store owner"
      iwantto "add items back to stock when they're returned"
      sothat "..."
      
      scenario_refund "Refunded items should be returned to stock" {
        task TestTaskFactory.stockManagerTask()
      
        given "a list of black sweaters left in stock and customer returns the sweaters for a refund" {
          datatable {
            sweater.black | sweater.refund.black  | expect.sweater.balck
            3             | 1                     | 4
            1             | 10                    | 11
            100           | 50                    | 150
          }
          sweater.blue = 0
        }
        
        then "I should have expcected black sweaters in stock"{
          sweater.black << equalTo(expect.sweater.balck)
        }
      }
    }
  }
  
  @Test
  public void testTableWithShareData() {
    story() "Returns go to stock" {

      share{
        shared.data1
      }
            
      scenario_refund "Refunded items should be returned to stock" {
        task TestTaskFactory.stockManagerTask()
      
        given "read a shared data in given section shoudn't pause the parser thread" {
          datatable {
            sweater.black | sweater.refund.black  | expect.sweater.balck  | shared
            3             | 1                     | 4                     | shared.data1
            100           | 50                    | 150                   | shared.data1
          }
          sweater.blue = 0
        }
        
        then "I should have expcected black sweaters in stock"{
          sweater.black << equalTo(expect.sweater.balck)
        }
      }
    }
  }
  
  @Test
  public void testDataTableParamOverwrite() {
    story() "Returns go to stock" {
      inOrderTo "keep track of stock"
      asa "store owner"
      iwantto "add items back to stock when they're returned"
      sothat "..."
      
      scenario_refund "Refunded items should be returned to stock" {
        task TestTaskFactory.stockManagerTask()
      
        given "a list of black sweaters left in stock and customer returns the sweaters for a refund" {
          expect.sweater.balck = "expected.name.here"  //This will replace the table header
          datatable {            
            sweater.black | sweater.refund.black  | expect.sweater.balck
            3             | 1                     | 4
            1             | 10                    | 11
            100           | 50                    | 150
          }
          sweater.blue = 0
          sweater.refund.black = 1 //Data Table always overwrite individual parameter
        }
        
        then "I should have expected black sweaters in stock"{
          expect.sweater.balck << equalTo("expected.name.here")
          sweater.black << equalTo(expected.name.here)  //Working as this is the real parameter name in data table
          
          //sweater.black << equalTo(expect.sweater.balck)  //not working as expect.sweater.balck = "expected.name.here"          
        }
      }
    }
  }
  
  @Test
  public void testDataTableParamBatchAdd() {
    story() "DataTable support batch add" {
      inOrderTo "generate a lot of data table records"
      
      scenario_refund "Refunded items should be returned to stock" {
        task TestTaskFactory.stockManagerTask()
      
        given "a list of black sweaters left in stock and customer returns the sweaters for a refund" {
          expect.sweater.balck = "expected.name.here"  //This will replace the table header
          
          datatable {
            sweater.black | sweater.refund.black  | expect.sweater.balck
            3             | 1                     | 4
            1             | 10                    | 11
            100           | 50                    | 150
          }
          
          (1..200).each { number ->
            datatable {
              sweater.black | sweater.refund.black  | expect.sweater.balck
              number        | 1                     | number + 1
            }
          }
          
          sweater.blue = 0
          sweater.refund.black = 1 //Data Table always overwrite individual parameter
        }
        
        then "I should have expected black sweaters in stock"{
          println Thread.currentThread().getName() + " sweater.black=${sweater.black} sweater.black=${sweater.black} sweater.black=${sweater.black} sweater.black=${sweater.black} sweater.refund.black = ${sweater.refund.black}  expected.name.here = ${expected.name.here} Done, ${sweater.black} + ${sweater.refund.black} = ${expected.name.here}"
          expect.sweater.balck << equalTo("expected.name.here")
          sweater.black << equalTo(expected.name.here)  //Working as this is the real parameter name in data table
        }
      }
    }
  }
  
  @Test
  public void testWithTwoDataTables() {
    try{
      story() "Returns go to stock" {
        inOrderTo "keep track of stock"
        asa "store owner"
        iwantto "add items back to stock when they're returned"
        sothat "..."
        
        scenario_refund "Refunded items should be returned to stock" {
          task TestTaskFactory.stockManagerTask()
        
          given "I currently have black sweaters left in stock and customer returns the sweaters for a refund" {
            datatable {
              sweater.black | sweater.refund.black  | expect.sweater.balck
              3             | 1                     | 4
              1             | 10                    | 11
              100           | 50                    | 150
            }
            
            datatable {
              ColumnIsNotSame | sweater.refund.black  | expect.sweater.balck
              3             | 1                     | 4
              1             | 10                    | 11
              100           | 50                    | 150
            }
            
            sweater.blue = 0
          }
          
          then "I should have four black sweaters in stock"{
            sweater.black << equalTo(expect.sweater.balck)
          }
        }
      }
    }catch (DslException e){
      assert e.getMessage().contains("So far we support only one type of datatable in all given section");
      return;
    }
    
    fail("Should run into error")
  }
  
  @Test
  public void testDataTableWithOneRow() {
    try{
      story() "Returns go to stock" {
        inOrderTo "keep track of stock"
        asa "store owner"
        iwantto "add items back to stock when they're returned"
        sothat "..."
        
        scenario_refund "Refunded items should be returned to stock" {
          task TestTaskFactory.stockManagerTask()
        
          given "I currently have black sweaters left in stock and customer returns the sweaters for a refund" {
            datatable {
              sweater.black | sweater.refund.black  | expect.sweater.balck
            }
            sweater.blue = 0
          }
          
          then "I should have four black sweaters in stock"{
            sweater.black << equalTo(expect.sweater.balck)
          }
        }
      }    
    }catch (DslException e){
      assert e.getMessage().contains("datatable required at least 2 rows");
      return;
    }
    fail("Should run into error")
  }
  
}
