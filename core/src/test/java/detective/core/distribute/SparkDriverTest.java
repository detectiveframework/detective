package detective.core.distribute;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class SparkDriverTest {

  @Test
  public void test() {
    runningThreads.clear();
    SparkDriver.main(new String[]{"detective.core.distribute.collect"});
    
    checkThread("Story_1_Scenario_1 - Scenario1");
    checkThread("Story_1_Scenario_3 - Scenario1");
    checkThread("Story_1_Scenario_3 - Scenario2");
    checkThread("Story_1_Scenario_3 - Scenario3");
    checkThread("Story_2_FirstStory_Scenario_2 - Scenario1");
    checkThread("Story_2_FirstStory_Scenario_2 - Scenario2");
    checkThread("Story_2_SecondStory_Scenario_2 - Scenario1");
    checkThread("Story_2_SecondStory_Scenario_2 - Scenario2");
    checkThread("Story_2_Scenario_2_subpackage_FirstStory - Scenario1");
    checkThread("Story_2_Scenario_2_subpackage_FirstStory - Scenario2");
    checkThread("Story_2_Scenario_2_subpackage_SecondStory - Scenario1");
    checkThread("Story_2_Scenario_2_subpackage_SecondStory - Scenario2");
  }
  
  private Set<String> runningThreads = new HashSet<String>();
  private void checkThread(String scenarioName){
    String threadName = ScenarioThreadRecorder.getThread("sparkTest", scenarioName);
    Assert.assertNotNull("The story should run through and a thread name should put into a common store area", threadName);
    Assert.assertTrue("Should running in a spark worker thread", threadName.contains("worker"));
    
    //Assert.assertFalse("Every scenario should running in a different spark thread, thread name:" + threadName + " but already exists in list" + runningThreads.toString(), runningThreads.contains(threadName));
    runningThreads.add(threadName);
  }

}
