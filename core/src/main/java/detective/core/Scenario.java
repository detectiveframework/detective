package detective.core;

import groovy.lang.Closure;

import java.util.List;
import java.util.Map;

import detective.core.dsl.table.Row;

public interface Scenario extends Titled{
  
  public interface Step extends Titled{
    Closure<?> getExpectClosure();
  }
  
  String getId();
  
  boolean getSuccessed();
  void setSuccessed(boolean success);
  
  boolean getIgnored();
  void setIgnored(boolean ignored);
  
  Throwable getError();
  void setError(Throwable exception);
  
  Story getStory();
  
  List<Row> getScenarioTable();
  
  List<Step> getSteps();

}