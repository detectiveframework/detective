package detective.core.dsl;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import detective.core.Scenario.Context;

public class SimpleContext implements Context{

  private Map<String, Object> parameters = new HashMap<String, Object>(); 
  private String title;
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Map<String, Object> getParameters(){
    return ImmutableMap.copyOf(parameters);
  }
  
  public void addParameter(String key, Object value){
    this.parameters.put(key, value);
  }

  @Override
  public String toString() {
    return "\"" + title + "\"{\n      parameters=" + parameters + "}";
  }

}