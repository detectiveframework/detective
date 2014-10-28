package detective.core.distribute;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;

/**
 * The the result after a job ran in a spark cluster
 * 
 * @author James Luo
 *
 */
public class JobRunResult implements Serializable, Comparable<JobRunResult> {

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
      builder.append("error callstack:").append(getStackTrace(error)).append("\n");
    }
    
    return builder.toString();
  }
  
  private static String getStackTrace(Throwable aThrowable) {
    Writer result = new StringWriter();
    PrintWriter printWriter = new PrintWriter(result);
    aThrowable.printStackTrace(printWriter);
    return result.toString();
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

}
