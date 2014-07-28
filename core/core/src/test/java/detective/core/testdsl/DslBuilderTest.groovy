package detective.core.testdsl

import detective.core.Scenario
import detective.core.Story
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import static detective.core.dsl.builder.DslBuilder.*;
import detective.core.dsl.SharedDataPlaceHolder;

import static detective.core.Matchers.*;

import detective.core.TestTaskFactory;
import detective.core.dsl.DslException

public class DslBuilderTest {
  
  @Test
  public void dslSimpleStory(){
    story() "Returns go to stock" {
      inOrderTo "keep track of stock"
      asa "store owner"
      
      scenario_A "Simple scenario" {
        
      }
    }
  }
  
  @Test
  public void storyWithShareData(){
    Story story = story() "ShareDataInStory" {
      inOrderTo "Share data between scenario in story level"
      asa "store owner"
      
      share {
        sharedData1 = "This Is Shared Data1"
        shared.data2 = "shared data with property format"
        shared.placeholder = PLACEHOLDER
      }

      scenario_1 "data should able to share between scenario" {
        given "shared data above"{
          
        }
        given "give a value to shared.placeholder"{
          shared.placeholder = "Place Holder assigned in scenario"
        }
        then{
          sharedData1 << equalTo("This Is Shared Data1")
        }
      }
    }
    
    assert story.getSharedDataMap().size() == 3;
    assert story.getSharedDataMap().get("sharedData1") == "This Is Shared Data1"
    assert story.getSharedDataMap().get("shared.data2") == "shared data with property format"
    assert story.getSharedDataMap().get("shared.placeholder") == SharedDataPlaceHolder.INSTANCE;
  }
  
  /**
   * 
   */
  @Test
  public void dslBuilderTest(){
    Story story = story() "Returns go to stock" {
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
        }
        
        when "he returns the sweater for a refund" {
          sweater.refund.black = 1
          runtask TestTaskFactory.stockManagerTask()
        }
        
        then "I should have four black sweaters in stock"{
          sweater.balck = equalTo(4)
        }
      }
      
      scenario_replace "Replaced items should be returned to stock" {
        given "a customer buys a blue garment"
        given "I have two blue garments in stock"
        given "And three black garments in stock."
        when "he returns the garment for a replacement in black"
        then "I should have three blue garments in stock"
        then "And two black garments in stock"
      }
    }
    
    assert story != null;
    
    Assert.assertThat(story.title, equalTo("Returns go to stock"))
    Assert.assertThat(story.benefit, equalTo("add items back to stock when they're returned"))
    Assert.assertThat(story.role, equalTo("store owner"))
    Assert.assertThat(story.feature, equalTo("keep track of stock"))
    
    Assert.assertThat(story.scenarios.size(), equalTo(2));
    
    //Give
    Scenario s0 = story.scenarios.get(0);
    Assert.assertThat(s0.getId(), equalTo("scenario_refund"));
    Assert.assertThat(s0.getTitle(), equalTo("Refunded items should be returned to stock"));
    Assert.assertThat(s0.getTasks().size(), equalTo(3));
    Assert.assertThat(s0.getTasks().get(0).getClass().getName(), equalTo("detective.core.testdsl.stock.SweaterStockManagerTask"));
    Assert.assertThat(s0.getTasks().get(1).getClass().getName(), equalTo("detective.core.testdsl.stock.SweaterStockManagerTask"));
    Assert.assertThat(s0.getTasks().get(2).getClass().getName(), equalTo("detective.core.testdsl.stock.SweaterStockManagerTask"));
    Assert.assertThat(s0.getContexts().get(0).title, equalTo("a customer previously bought a black sweater from me"));
    Assert.assertThat(s0.getContexts().get(0).getParameters().size(), equalTo(0));
    Assert.assertThat(s0.getContexts().get(1).title, equalTo("I currently have three black sweaters left in stock"));
    Assert.assertThat(s0.getContexts().get(1).getParameters().size(), equalTo(1));
    Assert.assertThat(s0.getContexts().get(1).getParameters().get("sweater.black"), equalTo(3));
    
    //When
    Assert.assertThat(s0.getEvents().title, equalTo("he returns the sweater for a refund"));
    Assert.assertThat(s0.getEvents().getParameters().size(), equalTo(1));
    Assert.assertThat(s0.getEvents().getParameters().get("sweater.refund.black"), equalTo(1));
    
    Assert.assertThat(story.scenarios.get(1).getId(), equalTo("scenario_replace"));
    Assert.assertThat(story.scenarios.get(1).getTitle(), equalTo("Replaced items should be returned to stock"));
    
  }
  
  @Test
  public void dslBuilderTestParameterIssue(){
    try {
      Story story = story() "parameter not support sub . " {
        inOrderTo "remove the ambiguousness of proeprty access operation . "
        asa "detective designer"
        iwantto "stop my tester and developer running into this ambiguousness"
        sothat "..."
        forexample "for example login.username if a valid identifier for us, but when you add login.username.lastname, we have no idea it is going to access a property from identifier login.username or it is a new identifier. "
        
        scenario_A "define login.username first then login.username.lastname" {
          task TestTaskFactory.stockManagerTask()
          
          given "a customer previously bought a black sweater from me" {
            login.username = "username"
            login.username.lastname = "lastname"
          }
        }
      }
    } catch (DslException e){
      assert e.getMessage().contains("ambiguousness");
      return;
    }
    
    fail("Should run into error")
  }
  
  @Test
  public void dslBuilderTestParameterIssue1(){
    try {
      Story story = story() "parameter not support sub . " {
        inOrderTo "remove the ambiguousness of proeprty access operation . "
        asa "detective designer"
        iwantto "stop my tester and developer running into this ambiguousness"
        sothat "..."
        forexample "for example login.username if a valid identifier for us, but when you add login.username.lastname, we have no idea it is going to access a property from identifier login.username or it is a new identifier. "
        
        scenario_B "define login.username.lastname first and login.username" {
          task TestTaskFactory.stockManagerTask()
          
          given "a customer previously bought a black sweater from me" {
            login.username.lastname = "lastname"
                login.username = "username"
          }
        }
      }
    } catch (DslException e){
      assert e.getMessage().contains("ambiguousness");
      return;
    }
    
    fail("Should run into error")
  }

}

