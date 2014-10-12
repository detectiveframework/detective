package detective.core.runner;

import static org.junit.Assert.*;

import static detective.core.Detective.*;
import static detective.core.Matchers.*;

import detective.core.dsl.DslException
import detective.core.TestTaskFactory;

import detective.core.Story
import detective.core.StoryRunner
import java.util.concurrent.atomic.AtomicInteger
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
        
        scenarioTable {
          sweater.black | sweater.refund.black  | expect.sweater.balck
          3             | 1                     | 4
          1             | 10                    | 11
          100           | 50                    | 150
        }
        
        given "a list of black sweaters left in stock and customer returns the sweaters for a refund" {
          sweater.blue = 0
          
          runtask TestTaskFactory.stockManagerTask()
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
        scenarioTable {
          sweater.black | sweater.refund.black  | expect.sweater.balck  | shared
          3             | 1                     | 4                     | shared.data1
          100           | 50                    | 150                   | shared.data1
        }
        
        given "read a shared data in given section shoudn't pause the parser thread" {
          sweater.blue = 0
          runtask TestTaskFactory.stockManagerTask()
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
        scenarioTable {
          sweater.black | sweater.refund.black  | expect.sweater.balck
          3             | 1                     | 4
          1             | 10                    | 2
          100           | 50                    | 101
        }
      
        given "a list of black sweaters left in stock and customer returns the sweaters for a refund" {
          sweater.blue = 0
          sweater.refund.black = 1 //will overwrite table
          
          runtask TestTaskFactory.stockManagerTask()
        }
        
        then "I should have expected black sweaters in stock"{
          expect.sweater.balck << equalTo(expect.sweater.balck)
          sweater.black << equalTo(expect.sweater.balck)  //Working as this is the real parameter name in data table
        }
      }
    }
  }
  
  private AtomicInteger runningCounter = new AtomicInteger(0);
  
  @Test
  public void testDataTableParamBatchAdd() {
    story() "DataTable support batch add" {
      inOrderTo "generate a lot of data table records"
      
      scenario_refund "Refunded items should be returned to stock" {
        scenarioTable {
          sweater.black | sweater.refund.black  | expect.sweater.balck
          3             | 1                     | 4
          1             | 10                    | 11
          100           | 50                    | 150
        }
        
        (1..200).each { number ->
          scenarioTable {
            sweater.black | sweater.refund.black  | expect.sweater.balck
            number        | 2                     | number + 2
          }
        }
      
        given "a list of black sweaters left in stock and customer returns the sweaters for a refund" {
          sweater.blue = 0
          this.runningCounter.andIncrement;
          
          runtask TestTaskFactory.stockManagerTask()
        }
        
        then "I should have expected black sweaters in stock"{
          println Thread.currentThread().getName() + " sweater.black=${sweater.black} sweater.refund.black = ${sweater.refund.black}  expect.sweater.balck = ${expect.sweater.balck} Done, ${sweater.black} + ${sweater.refund.black} = ${expect.sweater.balck}"
          sweater.black << equalTo(expect.sweater.balck)  
        }
      }
    }
    
    assert this.runningCounter.get() == 203
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
          scenarioTable {
            sweater.black | sweater.refund.black  | expect.sweater.balck
            3             | 1                     | 4
            1             | 10                    | 11
            100           | 50                    | 150
          }
          
          scenarioTable {
            ColumnIsNotSame | sweater.refund.black  | expect.sweater.balck
            3             | 1                     | 4
            1             | 10                    | 11
            100           | 50                    | 150
          }
        
          given "I currently have black sweaters left in stock and customer returns the sweaters for a refund" {
            sweater.blue = 0
          }
          
          then "I should have four black sweaters in stock"{
            sweater.black << equalTo(expect.sweater.balck)
          }
        }
      }
    }catch (DslException e){
      assert e.getMessage().contains("Scenario table must have same header and columns");
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
          scenarioTable {
            sweater.black | sweater.refund.black  | expect.sweater.balck
          }
        
          given "I currently have black sweaters left in stock and customer returns the sweaters for a refund" {
            sweater.blue = 0
            
            runtask TestTaskFactory.stockManagerTask()
          }
          
          then "I should have four black sweaters in stock"{
            sweater.black << equalTo(expect.sweater.balck)
          }
        }
      }    
    }catch (DslException e){
      assert e.getMessage().contains("table requires at least 2 rows");
      return;
    }
    fail("Should run into error")
  }
  
}
