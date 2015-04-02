package detective.core.distribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import detective.utils.Utils;

/**
 * The the result after a job ran in a spark cluster
 * 
 * @author James Luo
 *
 */
public class JobRunResult implements Serializable, Comparable<JobRunResult> {
  
  public static class JobRunResultSteps implements Serializable{

    private static final long serialVersionUID = 1L;

    private String stepName;
    private boolean successed;
    private List<String> additionalMsgs = new ArrayList<String>();
    
    @Override
    public String toString() {
      return "JobRunResultSteps [stepName=" + stepName + ", successed=" + successed
          + ", additionalMsgs=" + additionalMsgs + "]";
    }
    public String getStepName() {
      return stepName;
    }
    public void setStepName(String stepName) {
      this.stepName = stepName;
    }
    public boolean isSuccessed() {
      return successed;
    }
    public void setSuccessed(boolean successed) {
      this.successed = successed;
    }
    public List<String> getAdditionalMsgs() {
      return additionalMsgs;
    }
    public void setAdditionalMsgs(List<String> additionalMsgs) {
      this.additionalMsgs = additionalMsgs;
    }
  }

  private static final long serialVersionUID = 1L;
  
  /**
   * The story name user defined.In JobToRun, it has only job class name
   */
  private String storyName;
  /**
   * The scenario name
   */
  private String scenarioName;
  
  /**
   * Success or not
   */
  private Boolean successed = false;

  /**
   * The error if any
   */
  private Throwable error;
  
  /**
   * ignored for some reason?
   */
  private boolean ignored = false;
  
  private List<JobRunResultSteps> steps = new ArrayList<JobRunResultSteps>();
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("JobRunResult [\n");
    
    builder.append("storyName:").append(storyName).append("\n ");
    builder.append("scenarioName:").append(scenarioName).append("\n ");
    builder.append("successed:").append(successed).append("\n ");
    builder.append("ignored:").append(ignored).append("\n ");
    if (error != null){
      builder.append("error:").append(error).append("\n ");
      builder.append("error callstack:").append(Utils.getStackTrace(error)).append("\n");
    }
    
    return builder.toString();
  }

  public String getStoryName() {
    return storyName;
  }

  public void setStoryName(String storyName) {
    this.storyName = storyName;
  }

  public String getScenarioName() {
    return scenarioName;
  }

  public void setScenarioName(String scenarioName) {
    this.scenarioName = scenarioName;
  }

  public Boolean getSuccessed() {
    return successed;
  }

  public void setSuccessed(Boolean successed) {
    this.successed = successed;
  }

  public Throwable getError() {
    return error;
  }

  public void setError(Throwable error) {
    this.error = error;
  }

  public boolean isIgnored() {
    return ignored;
  }

  public void setIgnored(boolean ignored) {
    this.ignored = ignored;
  }

  @Override
  public int compareTo(JobRunResult o) {
    return this.storyName.compareTo(o.getStoryName());
  }

  public List<JobRunResultSteps> getSteps() {
    return steps;
  }

  public void setSteps(List<JobRunResultSteps> steps) {
    this.steps = steps;
  }

}
