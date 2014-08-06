package detective.core.runner;

import static org.junit.Assert.*;
import static detective.core.Detective.*;
import static detective.core.Matchers.*;

import detective.core.dsl.DslException
import detective.core.TestTaskFactory;

import detective.core.Story
import detective.core.StoryRunner
import org.junit.Test;

public class ParallelRunnerTest {

  @Test
  public void testSharedDataShareProcess() {
    story() "Becareful for the parameter 'sharedata.changedin.thensection', it will cause thread pause all the time if echo task just simplly run through all parameters" {

      share { 
        echotask.task1
        
        sharedata.changedin.givesection
        sharedata.changedin.thensection
        sharedata.changedin.whensection
      }

      scenario_echo1 "echo1" {
        given "give a parameter" { 
          task1 = "parallel"
          sharedata.changedin.givesection = "I configed in give section"
        }

        when "A" { 
          runtask TestTaskFactory.echo()
          sharedata.changedin.whensection = "I configed in when section"
        }

        then "parameter should send back"{
          sharedata.changedin.thensection = "I configed in then section"
          echotask.task1 << equalTo("parallel")
        }
      }
    }
  }
  
  
  @Test
  public void testAutoDepends() {
    story() "Two Scenarios should able to run parallel and do auto depends" {

      share {
        echotask.task1
      }

      scenario_echo1 "echo1" {
        given "give a parameter" {
          task1 = "parallel"
        }

        when "A" { runtask TestTaskFactory.echo() }

        then "parameter should send back"{
          echotask.task1 << equalTo("parallel")
        }
      }

      scenario_echo2 "echo2" {
        given "give a parameter that come from share data and it will setup by echo1 task" {
          parameter1 = echotask.task1
        }

        when "AA"{ runtask TestTaskFactory.echo()  }

        then "parameter should send back"{
          parameter1 << "parallel"
          echotask.parameter1 << equalTo("parallel")
          echotask.parameter1 << equalTo(parameter1)
        }
      }
    }
  }
  
  @Test
  public void testAutoDependsOrderIsNotAProblem() {
    story() "Two Scenarios should able to run parallel and do auto depends" {

      def echo2Ran = false;
      
      share {
        echotask.task1
      }

      scenario_echo2 "echo2" {
        given "give a parameter that come from share data and it will setup by echo1 task" {
          givenValue = "given"
        }

        when "AA"{ runtask TestTaskFactory.echo()  }

        then "parameter should send back"{
          parameter1 = echotask.task1
          parameter1 << "parallel"
          echo2Ran = true;
          echotask.givenValue << equalTo("given")
          echotask.givenValue << equalTo(givenValue)
        }
      }
      
      scenario_echo1 "echo1" {
        given "give a parameter" {
          task1 = "parallel"
        }

        when "A" { runtask TestTaskFactory.echo() }

        then "parameter should send back"{
          echotask.task1 << equalTo("parallel")
        }
      }

      
    }
  }
  
  @Test
  public void testSharedDataMoreThanOnce() {
    try {
      story() "Shared data can't setup twice" {
        
        share {
          echotask.task1
        }
        
        scenario_echo1 "echo1" {
          given "give a parameter" {
            task1 = "parallel"
                echotask.task1 = "first time"
          }
          
          when "A" { runtask TestTaskFactory.echo() }
          
          then "parameter should send back"{
            echotask.task1 << equalTo("parallel")
          }
        }
      }
    } catch (Exception e) {
      assert e.getMessage().contains("[echotask.task1] can only setup once.");
      return;
    }
    
    //fail("Should run into error");
    //We allow them setup more then once now
  }

}
