package detective.core.distribute;

import detective.core.Story;

public class JobStoryRunContext {

  /**
   * The groovy script class
   */
  private String scriptClassName;
  
  /**
   * The job description currently trying to run
   */
  private Job job;
  
  /**
   * Current story which is trying to run
   */
  private Story story;
  
  private int currentStoryIndex;

  public String getScriptClassName() {
    return scriptClassName;
  }

  public void setScriptClassName(String scriptClass) {
    this.scriptClassName = scriptClass;
  }

  public Job getJob() {
    return job;
  }

  public void setJob(Job job) {
    this.job = job;
  }

  public Story getStory() {
    return story;
  }

  public void setStory(Story story) {
    this.story = story;
  }

  public int getCurrentStoryIndex() {
    return currentStoryIndex;
  }

  public void setCurrentStoryIndex(int currentStoryIndex) {
    this.currentStoryIndex = currentStoryIndex;
  }
  
  
  
}
