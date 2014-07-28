package detective.core.runner;

import static org.junit.Assert.*;
import static detective.core.dsl.builder.DslBuilder.*;
import static detective.core.Matchers.*;

import detective.core.dsl.DslException
import detective.core.TestTaskFactory;

import detective.core.Story
import detective.core.StoryRunner
import org.junit.Test;

public class ParallelRunnerTest {

  @Test
  public void test() {
    Story story1 = story() "Two Scenarios should able to run parallel" {
      inOrderTo "keep track of stock"
      asa "store owner"
      iwantto "add items back to stock when they're returned"
      sothat "..."
      
      scenario_echo1 "echo1" {
        task TestTaskFactory.echo()
      
        given "give a parameter" {
          parameter1 = "parallel"
        }
        
        then "parameter should send back"{
          echotask.parameter1 << equalTo("parallel")
          echotask.parameter1 << equalTo(parameter1)
        }
      }
      
      scenario_echo2 "echo2" {
        task TestTaskFactory.echo()
      
        given "give a parameter" {
          parameter1 = "parallel"
        }
        
        then "parameter should send back"{
          echotask.parameter1 << equalTo("parallel")
          echotask.parameter1 << equalTo(parameter1)
        }
      }
    }
    
    StoryRunner runner = new SimpleStoryRunner();
    
    runner.run(story1);
  }

}
