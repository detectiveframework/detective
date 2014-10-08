package detective.core.dsl.builder;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import detective.core.Parameters;
import detective.core.TestTask;
import detective.core.dsl.DslException;
import detective.core.dsl.SimpleScenario;
import detective.core.dsl.table.Row;
import detective.core.dsl.table.TableParser;
import detective.core.runner.PropertyToStringDelegate;

public class ScenarioDelegate extends ShareDataAwardDelegate{    
  protected String title;
  protected SimpleScenario scenario;
  protected Closure<?> closure;
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public SimpleScenario getScenario() {
    return scenario;
  }

  public void setScenario(SimpleScenario scenario) {
    this.scenario = scenario;
  }

  public Closure<?> getClosure() {
    return closure;
  }

  public void setClosure(Closure<?> closure) {
    this.closure = closure;
  }

  public ScenarioDelegate(Parameters values){
    super(values);
  }
  
  public ScenarioDelegate(PropertyToStringDelegate parent, String propertyName, Parameters values){
    super(parent, propertyName, values);
  }
  
  protected PropertyToStringDelegate newNextLevelProperty(PropertyToStringDelegate parent, String propertyName){
    return new ScenarioDelegate(parent, propertyName, values);
  }
  
}