package detective.core.distribute;

import detective.core.Scenario;
import detective.core.Story;

public class JobFactory {
  
  public static JobToRun newJob(Story story, int storyIndex){
    JobToRun job = new JobToRun();
    job.setStoryClassName(story.getClass().getName());
    job.setStoryIndex(storyIndex);
    job.setScenarioIndex(-1);
    return job;
  }
  
  public static JobToRun newJob(Scenario scenario, int storyIndex, int index){
    JobToRun job = newJob(scenario.getStory(), storyIndex);
    job.setScenarioIndex(index);
    return job;
  }

}
