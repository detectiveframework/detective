package detective.core.distribute.collect;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import detective.core.distribute.JobToRun;
import detective.core.distribute.collector.JobCollector;

public class JobCollectorTest {

  @Test
  public void testStoryWithMoreThen1Scenarios_ShouldRunInDifferentThreadInSpark() {
    List<JobToRun> jobs = JobCollector.collectAll("detective.core.distribute.collect");
    
    Assert.assertEquals(jobs.size(), 1 + 3 + 4 + 4);
    
    Assert.assertEquals(jobs.get(0).getStoryClassName(), "detective.core.distribute.collect.Story_1_Scenario_1");
    Assert.assertEquals(jobs.get(0).getStoryIndex(), 0);
    Assert.assertEquals(jobs.get(0).getScenarioIndex(), 0);
    Assert.assertEquals(jobs.get(0).getDataTableIndex(), -1);
    
    Assert.assertEquals(jobs.get(1).getStoryClassName(), "detective.core.distribute.collect.Story_1_Scenario_3");
    Assert.assertEquals(jobs.get(1).getStoryIndex(), 0);
    Assert.assertEquals(jobs.get(1).getScenarioIndex(), 0);
    
    Assert.assertEquals(jobs.get(2).getStoryClassName(), "detective.core.distribute.collect.Story_1_Scenario_3");
    Assert.assertEquals(jobs.get(2).getStoryIndex(), 0);
    Assert.assertEquals(jobs.get(2).getScenarioIndex(), 1);
    
    Assert.assertEquals(jobs.get(3).getStoryClassName(), "detective.core.distribute.collect.Story_1_Scenario_3");
    Assert.assertEquals(jobs.get(3).getStoryIndex(), 0);
    Assert.assertEquals(jobs.get(3).getScenarioIndex(), 2);
    
    //Story_2_Scenario_2
    Assert.assertEquals(jobs.get(4).getStoryClassName(), "detective.core.distribute.collect.Story_2_Scenario_2");
    Assert.assertEquals(jobs.get(4).getStoryIndex(), 0);
    Assert.assertEquals(jobs.get(4).getScenarioIndex(), 0);
    
    Assert.assertEquals(jobs.get(5).getStoryClassName(), "detective.core.distribute.collect.Story_2_Scenario_2");
    Assert.assertEquals(jobs.get(5).getStoryIndex(), 0);
    Assert.assertEquals(jobs.get(5).getScenarioIndex(), 1);
    
    Assert.assertEquals(jobs.get(6).getStoryClassName(), "detective.core.distribute.collect.Story_2_Scenario_2");
    Assert.assertEquals(jobs.get(6).getStoryIndex(), 1);
    Assert.assertEquals(jobs.get(6).getScenarioIndex(), 0);
    
    Assert.assertEquals(jobs.get(7).getStoryClassName(), "detective.core.distribute.collect.Story_2_Scenario_2");
    Assert.assertEquals(jobs.get(7).getStoryIndex(), 1);
    Assert.assertEquals(jobs.get(7).getScenarioIndex(), 1);
    
    //Subpackage
    Assert.assertEquals(jobs.get(8).getStoryClassName(), "detective.core.distribute.collect.subpackage.Story_2_Scenario_2_subpackage");
    Assert.assertEquals(jobs.get(8).getStoryIndex(), 0);
    Assert.assertEquals(jobs.get(8).getScenarioIndex(), 0);
    
    Assert.assertEquals(jobs.get(9).getStoryClassName(), "detective.core.distribute.collect.subpackage.Story_2_Scenario_2_subpackage");
    Assert.assertEquals(jobs.get(9).getStoryIndex(), 0);
    Assert.assertEquals(jobs.get(9).getScenarioIndex(), 1);
    
    Assert.assertEquals(jobs.get(10).getStoryClassName(), "detective.core.distribute.collect.subpackage.Story_2_Scenario_2_subpackage");
    Assert.assertEquals(jobs.get(10).getStoryIndex(), 1);
    Assert.assertEquals(jobs.get(10).getScenarioIndex(), 0);
    
    Assert.assertEquals(jobs.get(11).getStoryClassName(), "detective.core.distribute.collect.subpackage.Story_2_Scenario_2_subpackage");
    Assert.assertEquals(jobs.get(11).getStoryIndex(), 1);
    Assert.assertEquals(jobs.get(11).getScenarioIndex(), 1);
  }

}
