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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParameterResolveTest {

  @Test
  public void testParameter() {
    story() "Parameter Resolve from Config file" {
      scenario_echo1 "Read parameter values from config file if can't found in current contet" {
        given "give a parameter" { 
          task1 = "parallel" 
          parameterFromConfig = parameter.resolve.test.givenSecionParameter
          parameter.resolve.test.givenOverrided = "if has same name here will overwrite config file"
        }

        when "run echo task" { runtask TestTaskFactory.echo() }

        then "parameter should send back"{
          task1 << "parallel"
          echotask.task1 << "parallel"
          
          echotask.parameterFromConfig << parameter.resolve.test.givenSecionParameter
          echotask.parameterFromConfig << "Given"
          echotask.parameterFromConfig << parameterFromConfig
          
          parameter.resolve.test.givenOverrided << "if has same name here will overwrite config file"
          
          ElasticSearchServer.port << 9999
        }
      }
    }
  }
}
