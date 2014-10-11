package detective.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import detective.core.attation.AttationTest;
import detective.core.distribute.collect.JobCollectorTest;
import detective.core.runner.DataSharedInStoryLevelTest;
import detective.core.runner.DataTableRunnerTest;
import detective.core.runner.JsonSupportTest;
import detective.core.runner.MultTasksTest;
import detective.core.runner.ParallelRunnerTest;
import detective.core.runner.ParameterResolveTest;
import detective.core.runner.RunSpecificScenarioOnly;
import detective.core.runner.SimpleStoryRunnerTest;
import detective.core.runner.TableParserTest;
import detective.core.story.RunTestFromScriptTest;
import detective.core.testdsl.DslBuilderTest;

@RunWith(Suite.class)
@SuiteClasses(
  {
    DslBuilderTest.class,
    RunTestFromScriptTest.class,
    SimpleStoryRunnerTest.class,
    TableParserTest.class,
    DataTableRunnerTest.class,
    DataSharedInStoryLevelTest.class,
    AttationTest.class,
    ParallelRunnerTest.class,
    JsonSupportTest.class,
    MultTasksTest.class,
    ParameterResolveTest.class,
    JobCollectorTest.class,
    
    //groovy
    RunSpecificScenarioOnly.class
  })
public class AllDslTests {

}
