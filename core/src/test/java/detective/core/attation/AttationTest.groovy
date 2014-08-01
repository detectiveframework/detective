package detective.core.attation;

import static org.junit.Assert.*;
import static detective.core.Detective.*;
import static detective.core.Matchers.*;

import detective.core.dsl.DslException
import detective.core.TestTaskFactory;

import detective.core.Story
import detective.core.StoryRunner
import org.junit.Test;

public class AttationTest {

  @Test
  public void testSharedDataShareProcess() {
    story() "Two Scenarios should able to run parallel and do that auto depends" {

      share { 
        //echotask.task1  //cause dead lock
        
        sharedata.changedin.givesection
        sharedata.changedin.whensection
        //sharedata.changedin.thensection    //this will cause the thread pause all the time
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
          sharedata.changedin.thensection << "I configed in then section"
        }
      }
    }
  }

}
