package detective.core.runner;

import static org.junit.Assert.*;
import static detective.core.dsl.builder.DslBuilder.*;
import static detective.core.Matchers.*;

import detective.core.dsl.DslException
import detective.core.TestTaskFactory;

import detective.core.Story
import detective.core.StoryRunner
import org.junit.Test; 

public class RunnerNewFeatureRequestTest {

  //@Test
  public void testSimplerDsl() {
    Story story1 = story() "should able to ommit description words in given/when/then" {
      
      scenario_a1 "aa" {
        task TestTaskFactory.echo()
      
        given {
          parameter1 = "Shared"
        }
        when {
          
        }
        then {
          echotask.parameter1 << equalTo("Shared")
          echotask.parameter1 << equalTo(parameter1)
        }
      }
      
    }
    
    StoryRunner runner = new SimpleStoryRunner();
    
    runner.run(story1);
  }
  
  //@Test
  public void testSupportMoreSectionForThen() {
    Story story1 = story() "should able to ommit description words in given/when/then" {
      
      scenario_a1 "aa" {
        task TestTaskFactory.echo()
      
        given {
          parameter1 = "Shared"
        }
        when {
          
        }
        then {
          echotask.parameter1 << equalTo("Shared")
          echotask.parameter1 << equalTo(parameter1)
        }
        then {
          echotask.parameter1 << equalTo("Shared")
          echotask.parameter1 << equalTo(parameter1)
        }
      }
      
    }
    
    StoryRunner runner = new SimpleStoryRunner();
    
    runner.run(story1);
  }

}
