package detective.core.distribute;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class SparkDriverTest {
  
  @Test
  public void testResultSetCollectionFromDifferentMachine(){
    List<JobRunResult> results = SparkDriver.runJobs("detective.core.distribute.jobresult", "UnitTest_detective.core.distribute.jobresult");
    Assert.assertTrue(results.size() == 2);
    
    Assert.assertEquals(results.get(0).getStoryName(), "Story For Testing Collect Running Result from Different Threads / Machines");
    Assert.assertEquals(results.get(0).getScenarioName(), "Successed Scenario");
    Assert.assertEquals(results.get(0).getSuccessed(), true);
    Assert.assertEquals(results.get(0).getSteps().size(), 3); 
    Assert.assertEquals(results.get(0).getSteps().get(0).getStepName(), "step1 log a user message");
    Assert.assertEquals(results.get(0).getSteps().get(0).getAdditionalMsgs().size(), 1);
    Assert.assertEquals(results.get(0).getSteps().get(0).getAdditionalMsgs().get(0), "This is the message will display to end user");
    Assert.assertEquals(results.get(0).getSteps().get(1).getStepName(), "run echo task");
    Assert.assertEquals(results.get(0).getSteps().get(1).getAdditionalMsgs().size(), 1);
    Assert.assertEquals(results.get(0).getSteps().get(1).getAdditionalMsgs().get(0), "This is the message from task");
    Assert.assertEquals(results.get(0).getSteps().get(2).getStepName(), "I can check message which will echo back");
    Assert.assertEquals(results.get(0).getSteps().get(2).getAdditionalMsgs().size(), 0);
    
    Assert.assertEquals(results.get(1).getStoryName(), "Story For Testing Collect Running Result from Different Threads / Machines");
    Assert.assertEquals(results.get(1).getScenarioName(), "Failed Scenario");
    Assert.assertEquals(results.get(1).getSuccessed(), false);
    Assert.assertEquals(results.get(1).getSteps().size(), 3); 
    Assert.assertEquals(results.get(1).getSteps().get(0).getStepName(), "step1 log a user message");
    Assert.assertEquals(results.get(1).getSteps().get(0).getAdditionalMsgs().size(), 1);
    Assert.assertEquals(results.get(1).getSteps().get(0).getAdditionalMsgs().get(0), "This is the message will display to end user");
    Assert.assertEquals(results.get(1).getSteps().get(1).getStepName(), "through exception");
    Assert.assertEquals(results.get(1).getSteps().get(1).getAdditionalMsgs().size(), 0);
    Assert.assertEquals(results.get(1).getSteps().get(1).isSuccessed(), false);
    Assert.assertEquals(results.get(1).getSteps().get(2).getStepName(), "There is no chance this step will been executed, but this step will show in console so that user know one step has been scaped");
    Assert.assertEquals(results.get(1).getSteps().get(2).getAdditionalMsgs().size(), 0);
    Assert.assertEquals(results.get(1).getSteps().get(2).isSuccessed(), false);
  }

  @Test
  public void testGiveAPackageNameAllTestShouldRunInDifferentThread() {
    runningThreads.clear();
    Long errors = SparkDriver.run(new String[]{"detective.core.distribute.collect"});
    
    Assert.assertEquals(errors.intValue(), 1);
    
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
