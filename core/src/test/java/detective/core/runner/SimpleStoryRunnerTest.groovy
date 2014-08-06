package detective.core.runner;

import static org.junit.Assert.*;
import static detective.core.Detective.*;
import static detective.core.Matchers.*;
import detective.core.dsl.DslException
import detective.core.exception.StoryFailException;
import detective.core.TestTaskFactory;
import detective.core.Story
import detective.core.StoryRunner

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleStoryRunnerTest {
  
  
  
  @Before
  public void setUp() throws Exception {
    
  }

  @After
  public void tearDown() throws Exception {
    
  }

  @Test
  public void test() {
    story() "Returns go to stock" {
      inOrderTo "keep track of stock"
      asa "store owner"
      iwantto "add items back to stock when they're returned"
      sothat "..."
      
      scenario_refund "Refunded items should be returned to stock" {
        task TestTaskFactory.stockManagerTask()
      
        given "a customer previously bought a black sweater from me" {
          
        }
        
        given "I currently have three black sweaters left in stock" {
          sweater.black = 3
          sweater.blue = 0
        }
        
        when "he returns the sweater for a refund" {
          sweater.refund.black = 1
        }
        
        then "I should have four black sweaters in stock"{
          sweater.refund.black << equalTo(1)
          sweater.black << equalTo(4)
          sweater.black << 4
        }
      }
    }
  }
  
  @Test
  public void testSharedData(){
    Story story = story() "ShareDataInStory" {
      inOrderTo "Share data between scenario in story level"
      asa "store owner"
      
      share {
        sharedData1 = "This Is Shared Data1"
        shared.data2 = "shared data with property format"
        shared.placeholder
      }
      
      scenario "data should able to share between scenario" {
        task TestTaskFactory.echo()
        
        given "shared data above"{
          
        }
        given "give a value to shared.placeholder"{
          shared.placeholder = "Place Holder assigned in scenario"
        }
        then "Shared data should updated and should able to exam"{
          sharedData1 << equalTo("This Is Shared Data1")
          echotask.sharedData1 << equalTo("This Is Shared Data1")
          shared.data2 << equalTo("shared data with property format")
          echotask.shared.data2 << equalTo("shared data with property format")
          shared.placeholder << equalTo("Place Holder assigned in scenario")
          echotask.shared.placeholder << equalTo("Place Holder assigned in scenario")
        }
      }
    }
  }
  
  @Test
  public void testBeforeAfter(){
    Story story = story() "ShareDataInStory" {
      inOrderTo "Share data between scenario in story level"
      asa "store owner"
      
      share {
        sharedData1 = "This Is Shared Data1"
        shared.data2 = "shared data with property format"
        shared.placeholder
        loggedin
      }
      
      before "login" {
        task TestTaskFactory.echo()
        given "setup shared data"{
          loggedin = "James Luo"
        }
        then "shared data should updated" {
          loggedin << equalTo("James Luo")
        }
      }
      
      after "shutdown" {
        task TestTaskFactory.echo()
        
        then "shared data should updated" {
          loggedin << equalTo("James Luo")
        }
      }
      
      scenario "data should able to share between scenario" {
        task TestTaskFactory.echo()
        
        given "shared data above"{
          
        }
        given "give a value to shared.placeholder"{
          shared.placeholder = "Place Holder assigned in scenario"
        }
        then "Shared data should updated and should able to exam"{
          sharedData1 << equalTo("This Is Shared Data1")
          echotask.sharedData1 << equalTo("This Is Shared Data1")
          shared.data2 << equalTo("shared data with property format")
          echotask.shared.data2 << equalTo("shared data with property format")
          shared.placeholder << equalTo("Place Holder assigned in scenario")
          echotask.shared.placeholder << equalTo("Place Holder assigned in scenario")
        }
      }
    }
  }
  
  @Test
  public void testWrongPropertyName() {
    try{
      story() "Returns go to stock" {
        inOrderTo "keep track of stock"
        asa "store owner"
        iwantto "add items back to stock when they're returned"
        sothat "..."
        
        scenario_refund "Refunded items should be returned to stock" {
          task TestTaskFactory.stockManagerTask()
        
          given "a customer previously bought a black sweater from me" {
            
          }
          
          given "I currently have three black sweaters left in stock" {
            sweater.black = 3
            sweater.blue = 0
          }
          
          when "he returns the sweater for a refund" {
            sweater.refund.black = 1
          }
          
          then "I should have four black sweaters in stock"{
            sweater.refund.black << equalTo(1)
            sweater.balck << equalTo(4)    //<<<<<Here we have a wrong name
          }
        }
      }
    }catch (StoryFailException e){
      //Should show user with suggestion
      assert e.getMessage().contains("sweater.balck not able to found");
      assert e.getMessage().contains("do you mean : sweater.black");
      return;
    }
    
    fail("Should run into error")
  }
  
  @Test
  public void testWrongPropertyName1() {
    try{
      story() "Returns go to stock" {
        
        scenario_refund "Refunded items should be returned to stock" {
          task TestTaskFactory.stockManagerTask()
        
          given "a customer previously bought a black sweater from me" {
            
          }
          
          given "I currently have three black sweaters left in stock" {
            sweater.black = 3
            sweater.blue = 0
          }
          
          when "he returns the sweater for a refund" {
            sweater.refund.black = 1
          }
          
          then "I should have four black sweaters in stock"{
            sweater.refund.black << equalTo(1)
            sweater.black << equalTo(sweater.refund.black_wrong)    //<<<<<Here we have a wrong name
          }
        }
      }
    }catch (StoryFailException e){
      //Should show user with suggestion
      assert e.getMessage().contains("sweater.refund.black_wrong not able to found");
      assert e.getMessage().contains("do you mean : sweater.refund.black");
      return;
    }
    
    fail("Should run into error")
  }
  
  @Test
  public void testEmptyTasks() {
    try{
      story() "Returns go to stock" {
        inOrderTo "keep track of stock"
        asa "store owner"
        iwantto "add items back to stock when they're returned"
        sothat "..."
        
        scenario_refund "Refunded items should be returned to stock" {
          
          given "a customer previously bought a black sweater from me" {
            
          }
        }
      }
    }catch (StoryFailException e){
      assert e.getMessage().contains("You need at least 1 task defined in task section");
      return;
    }
    
    fail("Should run into error")
  }
  
  @Test
  public void testWrongTasks() {
    try{
      story() "Returns go to stock" {
        inOrderTo "keep track of stock"
        asa "store owner"
        iwantto "add items back to stock when they're returned"
        sothat "..."
        
        scenario_refund "Refunded items should be returned to stock" {
          task "This Is Wrong Task"
          given "a customer previously bought a black sweater from me" {
            
          }
        }
      }
    }catch (DslException e){
      assert e.getMessage().contains("implement interface TestTask and your class is java.lang.String");
      return;
    }
    
    fail("Should run into error")
  }

}
