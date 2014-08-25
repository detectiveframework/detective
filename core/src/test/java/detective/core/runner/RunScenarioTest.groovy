package detective.core.runner;

import static org.junit.Assert.*;
import static detective.core.Detective.*;
import static detective.core.Matchers.*;
import detective.core.dsl.DslException
import detective.core.exception.StoryFailException;
import detective.core.task.JsonBuilderTask
import detective.core.TestTaskFactory;
import detective.core.Story
import detective.core.StoryRunner
import detective.core.dsl.builder.DslBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RunScenarioTest {

  @Test
  public void testSimpleTasks() {
    Story story = DslBuilder.story() "You can run multiple tasks in a scenario" {
      scenario "Simplly run multiple tasks" {
        given "echo 1" { 
          parameter.given1 = "given1"
          runtask TestTaskFactory.echo()
        }
        
        then "It will run echo twice and the output of first task will become input of second task"{
          echotask.parameter.given1 << "given1"
        }
      }
    }
    
    
  }
  
  @Test
  public void testTasksWithDataTable() {
    story() "You can run multiple tasks in a scenario" {
      scenario "Simplly run multiple tasks" {
        """
          There is no order or parameters but the tasks do have order.
          the input of echo 1, it is actually contains both parameter.given1 and parameter.given2 as there is no order
        """
        given "echo 1" {
          parameter.given1 = "given1"
          runtask TestTaskFactory.echo()
        }
        
        given "echo 2"{
          datatable {
           given2.col1 | given2.col2 | given2.expected 
           1           | 2           | 3
           4           | 5           | 9
           10          | 11          | 21 
          }
          parameter.given2 = "given2"
          runtask TestTaskFactory.echo()
        }

        then "It will run echo twice and the output of first task will become input of second task"{
          echotask.parameter.given1 << "given1"
          echotask.echotask.parameter.given1 << "given1"
          echotask.parameter.given2 << "given2"
          echotask.echotask.parameter.given2 << "given2"
          
          echotask.given2.expected  << (echotask.given2.col1 + echotask.given2.col2)
        }
      }
    }
  }
}
