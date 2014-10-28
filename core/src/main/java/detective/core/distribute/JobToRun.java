package detective.core.distribute;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A Job Description and Job Result which will pass into Spark Cluster.
 * 
 * @author James Luo
 *
 */
public class JobToRun implements Serializable{
  
  private static final long serialVersionUID = 1L;

  /**
   * A story class name, this is groovy script but maybe contains more then one story or feature on it
   */
  private String storyClassName;
  
  /**
   * Which story you'd like to run
   */
  private int storyIndex = 0;
  
  /**
   * Which scenario you'd like to run
   * -1 means all scenarios
   */
  private int scenarioIndex = -1;
  
  /**
   * What parameters you'd like to use, this has highest priority, will overwrite everything
   */
  private Map<String, Object> parameters = new HashMap<String, Object>();
  
  /**
   * Which row of your dataTable you'd like to run
   * -1 means all table rows
   */
  private int dataTableIndex = -1;
  
  /**
   * The result runner put in
   */
  private JobRunResult jobResult;

  @Override
  public String toString() {
    return "JobToRun [storyClassName=" + storyClassName + ", storyIndex=" + storyIndex
        + ", scenarioIndex=" + scenarioIndex + ", dataTableIndex=" + dataTableIndex
        + ", jobResult=" + jobResult + ", parameters=" + parameters + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + dataTableIndex;
    result = prime * result + ((jobResult == null) ? 0 : jobResult.hashCode());
    result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
    result = prime * result + scenarioIndex;
    result = prime * result + ((storyClassName == null) ? 0 : storyClassName.hashCode());
    result = prime * result + storyIndex;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    JobToRun other = (JobToRun) obj;
    if (dataTableIndex != other.dataTableIndex)
      return false;
    if (jobResult == null) {
      if (other.jobResult != null)
        return false;
    } else if (!jobResult.equals(other.jobResult))
      return false;
    if (parameters == null) {
      if (other.parameters != null)
        return false;
    } else if (!parameters.equals(other.parameters))
      return false;
    if (scenarioIndex != other.scenarioIndex)
      return false;
    if (storyClassName == null) {
      if (other.storyClassName != null)
        return false;
    } else if (!storyClassName.equals(other.storyClassName))
      return false;
    if (storyIndex != other.storyIndex)
      return false;
    return true;
  }

  public String getStoryClassName() {
    return storyClassName;
  }

  public void setStoryClassName(String storyClassName) {
    this.storyClassName = storyClassName;
  }

  public int getStoryIndex() {
    return storyIndex;
  }

  public void setStoryIndex(int storyIndex) {
    this.storyIndex = storyIndex;
  }

  public int getScenarioIndex() {
    return scenarioIndex;
  }

  public void setScenarioIndex(int scenarioIndex) {
    this.scenarioIndex = scenarioIndex;
  }

  public int getDataTableIndex() {
    return dataTableIndex;
  }

  public void setDataTableIndex(int dataTableIndex) {
    this.dataTableIndex = dataTableIndex;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public JobRunResult getJobResult() {
    return jobResult;
  }

  public void setJobResult(JobRunResult jobResult) {
    this.jobResult = jobResult;
  }


}
