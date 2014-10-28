package detective.core.distribute.collect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import detective.core.distribute.JobToRun;
import detective.core.distribute.collector.JobCollector;

public class JobCollectorTest {

  @Test
  public void testStoryWithMoreThen1Scenarios_ShouldRunInDifferentThreadInSpark() {
    List<JobToRun> jobs = JobCollector.collectAll("detective.core.distribute.collect");
    
    Assert.assertEquals(jobs.size(), 1 + 3 + 4 + 4);
    
    Map<String, List<JobToRun>> sotryClassNameJobMap = new HashMap<String, List<JobToRun>>();
    for (JobToRun job : jobs){
      if (!sotryClassNameJobMap.containsKey(job.getStoryClassName())){
        sotryClassNameJobMap.put(job.getStoryClassName(), new ArrayList<JobToRun>());
      }
      sotryClassNameJobMap.get(job.getStoryClassName()).add(job);
    }
    
    List<JobToRun> jobList = sotryClassNameJobMap.get("detective.core.distribute.collect.Story_1_Scenario_1");
    Assert.assertEquals(jobList.get(0).getStoryClassName(), "detective.core.distribute.collect.Story_1_Scenario_1");
    Assert.assertEquals(jobList.get(0).getStoryIndex(), 0);
    Assert.assertEquals(jobList.get(0).getScenarioIndex(), 0);
    Assert.assertEquals(jobList.get(0).getDataTableIndex(), -1);
    
    jobList = sotryClassNameJobMap.get("detective.core.distribute.collect.Story_1_Scenario_3");
    Assert.assertEquals(jobList.get(0).getStoryClassName(), "detective.core.distribute.collect.Story_1_Scenario_3");
    Assert.assertEquals(jobList.get(0).getStoryIndex(), 0);
    Assert.assertEquals(jobList.get(0).getScenarioIndex(), 0);
    
    Assert.assertEquals(jobList.get(1).getStoryClassName(), "detective.core.distribute.collect.Story_1_Scenario_3");
    Assert.assertEquals(jobList.get(1).getStoryIndex(), 0);
    Assert.assertEquals(jobList.get(1).getScenarioIndex(), 1);
    
    Assert.assertEquals(jobList.get(2).getStoryClassName(), "detective.core.distribute.collect.Story_1_Scenario_3");
    Assert.assertEquals(jobList.get(2).getStoryIndex(), 0);
    Assert.assertEquals(jobList.get(2).getScenarioIndex(), 2);
    
    //Story_2_Scenario_2
    jobList = sotryClassNameJobMap.get("detective.core.distribute.collect.Story_2_Scenario_2");
    Assert.assertEquals(jobList.get(0).getStoryClassName(), "detective.core.distribute.collect.Story_2_Scenario_2");
    Assert.assertEquals(jobList.get(0).getStoryIndex(), 0);
    Assert.assertEquals(jobList.get(0).getScenarioIndex(), 0);
    
    Assert.assertEquals(jobList.get(1).getStoryClassName(), "detective.core.distribute.collect.Story_2_Scenario_2");
    Assert.assertEquals(jobList.get(1).getStoryIndex(), 0);
    Assert.assertEquals(jobList.get(1).getScenarioIndex(), 1);
    
    Assert.assertEquals(jobList.get(2).getStoryClassName(), "detective.core.distribute.collect.Story_2_Scenario_2");
    Assert.assertEquals(jobList.get(2).getStoryIndex(), 1);
    Assert.assertEquals(jobList.get(2).getScenarioIndex(), 0);
    
    Assert.assertEquals(jobList.get(3).getStoryClassName(), "detective.core.distribute.collect.Story_2_Scenario_2");
    Assert.assertEquals(jobList.get(3).getStoryIndex(), 1);
    Assert.assertEquals(jobList.get(3).getScenarioIndex(), 1);
    
    //Subpackage
    jobList = sotryClassNameJobMap.get("detective.core.distribute.collect.subpackage.Story_2_Scenario_2_subpackage");
    Assert.assertEquals(jobList.get(0).getStoryClassName(), "detective.core.distribute.collect.subpackage.Story_2_Scenario_2_subpackage");
    Assert.assertEquals(jobList.get(0).getStoryIndex(), 0);
    Assert.assertEquals(jobList.get(0).getScenarioIndex(), 0);
    
    Assert.assertEquals(jobList.get(1).getStoryClassName(), "detective.core.distribute.collect.subpackage.Story_2_Scenario_2_subpackage");
    Assert.assertEquals(jobList.get(1).getStoryIndex(), 0);
    Assert.assertEquals(jobList.get(1).getScenarioIndex(), 1);
    
    Assert.assertEquals(jobList.get(2).getStoryClassName(), "detective.core.distribute.collect.subpackage.Story_2_Scenario_2_subpackage");
    Assert.assertEquals(jobList.get(2).getStoryIndex(), 1);
    Assert.assertEquals(jobList.get(2).getScenarioIndex(), 0);
    
    Assert.assertEquals(jobList.get(3).getStoryClassName(), "detective.core.distribute.collect.subpackage.Story_2_Scenario_2_subpackage");
    Assert.assertEquals(jobList.get(3).getStoryIndex(), 1);
    Assert.assertEquals(jobList.get(3).getScenarioIndex(), 1);
  }

}
