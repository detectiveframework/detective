package detective.core.runner;

import static org.junit.Assert.*;

import static detective.core.dsl.builder.DslBuilder.*;
import static detective.core.Matchers.*;

import detective.core.dsl.DslException
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
    Story story1 = story() "Returns go to stock" {
      inOrderTo "keep track of stock"
      asa "store owner"
      iwantto "add items back to stock when they're returned"
      sothat "..."
      
      scenario_refund "Refunded items should be returned to stock" {
        task TestTaskFactory.stockManagerTask()
        task TestTaskFactory.stockManagerTask1()
      
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
        }
      }
    }
    
    StoryRunner runner = new SimpleStoryRunner();
    
    runner.run(story1);
  }
  
  @Test
  public void testSharedData(){
    Story story = story() "ShareDataInStory" {
      inOrderTo "Share data between scenario in story level"
      asa "store owner"
      
      share {
        sharedData1 = "This Is Shared Data1"
        shared.data2 = "shared data with property format"
        shared.placeholder = PLACEHOLDER
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
    
    StoryRunner runner = new SimpleStoryRunner();
    
    runner.run(story);
  }
  
  @Test
  public void testBeforeAfter(){
    Story story = story() "ShareDataInStory" {
      inOrderTo "Share data between scenario in story level"
      asa "store owner"
      
      share {
        sharedData1 = "This Is Shared Data1"
        shared.data2 = "shared data with property format"
        shared.placeholder = PLACEHOLDER
        loggedin = PLACEHOLDER
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
    
    StoryRunner runner = new SimpleStoryRunner();
    
    runner.run(story);
  }
  
  @Test
  public void testWrongPropertyName() {
    Story story1 = story() "Returns go to stock" {
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
    
    StoryRunner runner = new SimpleStoryRunner();
    
    try{
      runner.run(story1);
    }catch (DslException e){
      //Should show user with suggestion
      assert e.getMessage().contains("sweater.balck not able to found");
      assert e.getMessage().contains("do you mean : sweater.black");
      return;
    }
    
    fail("Should run into error")
  }
  
  @Test
  public void testEmptyTasks() {
    Story story1 = story() "Returns go to stock" {
      inOrderTo "keep track of stock"
      asa "store owner"
      iwantto "add items back to stock when they're returned"
      sothat "..."
      
      scenario_refund "Refunded items should be returned to stock" {
        
        given "a customer previously bought a black sweater from me" {
          
        }        
      }
    }
    
    StoryRunner runner = new SimpleStoryRunner();
    
    try{
      runner.run(story1);
    }catch (DslException e){
      assert e.getMessage().contains("You need at least 1 task defined in task section");
      return;
    }
    
    fail("Should run into error")
  }
  
  @Test
  public void testWrongTasks() {
    try{
      Story story1 = story() "Returns go to stock" {
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
