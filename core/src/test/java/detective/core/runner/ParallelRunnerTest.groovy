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
  public void test() {
    story() "Two Scenarios should able to run parallel and do that auto depends" {

      share { echotask.task1 }

      scenario_echo1 "echo1" {
        given "give a parameter" { task1 = "parallel" }

        when "A" { runtask TestTaskFactory.echo() }

        then "parameter should send back"{
          echotask.task1 << equalTo("parallel")
        }
      }

      scenario_echo2 "echo2" {
        given "give a parameter that come from share data and it will setup by echo1 task" { parameter1 = echotask.task1 }

        when "AA"{ runtask TestTaskFactory.echo()  }

        then "parameter should send back"{
//          echotask.parameter1 << equalTo("parallel")
//          echotask.parameter1 << equalTo(parameter1)
        }
      }
    }
  }

  @Test
  public void testAutoDepends() {
    story() "Two Scenarios should able to run parallel and do that auto depends" {

      share { echotask.task1 }

      scenario_echo1 "echo1" {
        given "give a parameter" { task1 = "parallel" }

        when "A" { runtask TestTaskFactory.echo() }

        then "parameter should send back"{
          echotask.task1 << equalTo("parallel")
        }
      }

      scenario_echo2 "echo2" {
        given "give a parameter that come from share data and it will setup by echo1 task" { parameter1 = echotask.task1 }

        when "AA"{ runtask TestTaskFactory.echo()  }

        then "parameter should send back"{
//          echotask.parameter1 << equalTo("parallel")
//          echotask.parameter1 << equalTo(parameter1)
        }
      }
    }
  }
}
