package detective.core.distribute;

import groovy.lang.Script;

import org.junit.Test;

public class SparkJobDistributorTest {

  @Test
  public void testStoryWithMoreThen1Scenarios_ShouldRunInDifferentThreadInSpark() {
    SparkJobDistributor dis = new SparkJobDistributor();
    
    Script script = new StoryWithMoreScenarios();
    script.run();
    dis.distribute(null);
  }

}
