package detective.core.dsl;

import detective.core.Parameters;
import detective.core.Scenario.Context;

public class SimpleContext implements Context{

  private Parameters parameters = new ParametersImpl(); 
  private String title;
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Parameters getParameters(){
    return parameters.immutable();
  }
  
  public void addParameter(String key, Object value){
    this.parameters.put(key, value);
  }

  @Override
  public String toString() {
    return "\"" + title + "\"{\n      parameters=" + parameters + "}";
  }

}