package detective.core.runner;

import static org.junit.Assert.*;
import static detective.core.dsl.builder.DslBuilder.*;
import static detective.core.Matchers.*;

import detective.core.dsl.DslException
import detective.core.TestTaskFactory;

import detective.core.Story
import detective.core.StoryRunner
import org.junit.Test; 

public class DataSharedInStoryLevelTest {

  @Test
  public void test() {
    //Comment by James
    
    Story story1 = story() "Data Should able to shared between scenarios" {
      inOrderTo "share data between scenarios"
      asa "test designer"
      iwantto "create shared data in story or feature level"
      sothat "the data can shared between scenarios"
      shared "shared"
      
      scenario "aa" {
        task TestTaskFactory.echo()
      
        given "shared data as a String Shared" {
          parameter1 = "Shared"
        }
        
        then "parameter should send back"{
          echotask.parameter1 << equalTo("Shared")
          echotask.parameter1 << equalTo(parameter1)
        }
      }
      
      scenario "echo2" {
        task TestTaskFactory.echo()
      
        given "shared data as a String" {
          parameter1 = "b"
        }
        
        then "parameter should send back"{
          echotask.parameter1 << equalTo("b")
          echotask.parameter1 << equalTo(parameter1)
        }
      }
    }
    
    StoryRunner runner = new SimpleStoryRunner();
    
    runner.run(story1, null);
  }

}
