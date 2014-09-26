package detective.core.distribute;

import detective.core.Scenario;
import detective.core.Story;

public class JobFactory {
  
  public static Job newJob(Story story, int storyIndex){
    Job job = new Job();
    job.setStoryClassName(story.getClass().getName());
    job.setStoryIndex(storyIndex);
    job.setScenarioIndex(-1);
    return job;
  }
  
  public static Job newJob(Scenario scenario, int storyIndex, int index){
    Job job = newJob(scenario.getStory(), storyIndex);
    job.setScenarioIndex(index);
    return job;
  }

}
