package detective.core;

import detective.core.attation.AttationTest;
import detective.core.distribute.collect.JobCollectorTest;
import detective.core.runner.*;
import detective.core.story.RunTestFromScriptTest;
import detective.core.task.HttpClientTaskTest;
import detective.core.testdsl.DslBuilderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by amila on 11/05/2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
  {HttpClientTaskTest.class})
public class HTTPClientTaskTests {
}
