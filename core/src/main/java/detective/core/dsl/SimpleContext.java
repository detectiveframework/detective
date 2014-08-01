package detective.core.dsl;

import detective.core.Parameters;
import detective.core.Scenario;
import detective.core.Scenario.Context;

public class SimpleContext implements Context{

  private final Parameters parameters; 
  private String title;
  private final Scenario scenario;
  
  public SimpleContext(Scenario scenario){
    this.scenario = scenario;
    parameters = new ParametersImpl(scenario.getStory().getSharedDataMap());
  }
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Parameters getParameters(){
    return parameters;
  }
  
  public void addParameter(String key, Object value){
    this.parameters.put(key, value);
  }

  @Override
  public String toString() {
    return "\"" + title + "\"{\n      parameters=" + parameters + "}";
  }

  public Scenario getScenario() {
    return scenario;
  }

}