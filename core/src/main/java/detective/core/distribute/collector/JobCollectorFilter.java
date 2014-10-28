package detective.core.distribute.collector;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import detective.core.Detective;
import detective.core.Scenario;
import detective.core.Story;
import detective.core.distribute.JobToRun;
import detective.core.distribute.JobFactory;
import detective.core.exception.StoryFailException;
import detective.core.filter.RunnerFilter;
import detective.core.filter.RunnerFilterChain;

public class JobCollectorFilter implements RunnerFilter<Story> {
  
  //private static final Logger logger = LoggerFactory.getLogger(JobCollectorFilter.class);
  
  private int currentSotryIndex = 0;
  
  private List<JobToRun> jobs = new ArrayList<JobToRun>();

  @Override
  public void doFilter(Story story, RunnerFilterChain<Story> chain) {
    try {
      distribute(story, currentSotryIndex);
      currentSotryIndex ++;
    } catch (Exception e) {
      Detective.error("Error to distribute story [" + story.getTitle() + "]" +  e.getMessage(), e);
    }
    
    chain.doFilter(story);
  }
  
  protected void distribute(Story story, int currentStoryIndex) {
    if (story.getSharedDataMap().size() > 0){
      jobs.add(JobFactory.newJob(story, currentStoryIndex));
    }else{
      for (int i = 0; i < story.getScenarios().size(); i++){
        Scenario scenario = story.getScenarios().get(i);
        jobs.add(JobFactory.newJob(scenario, currentStoryIndex, i)); 
      }
    }
  }
  
  public List<JobToRun> getJobs(){
    return jobs;
  }

}
