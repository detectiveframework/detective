package detective.core.distribute;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Job implements Serializable{
  
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
  
  
  @Override
  public String toString() {
    return "Job to run [storyClassName=" + storyClassName + ", storyIndex=" + storyIndex
        + ", scenarioIndex=" + scenarioIndex + ", dataTableIndex=" + dataTableIndex
        + ", parameters=" + parameters + "]";
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


}
