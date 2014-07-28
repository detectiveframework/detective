package detective.core.testdsl;

import static detective.core.Matchers.*;
import static detective.core.dsl.builder.DslBuilder.*;
import detective.core.Story
import detective.core.dsl.builder.DslBuilder;
import detective.core.TestTaskFactory;

public class TestStoryProvider {

  public static DslBuilder createWikiBDDSimpleStorySimple(){
    story "newStory" {
      abc "abc"
    }
  }
  
  public static Story createWikiBDDSimpleStory(){
    story() "Returns go to stock" {
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
  }
  
}
