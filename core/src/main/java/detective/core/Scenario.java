package detective.core;

import groovy.lang.Closure;

import java.util.List;
import java.util.Map;

public interface Scenario extends Titled{
  
  public interface Context extends Titled{
    /**
     * @return immutable map for parameters;
     */
    Parameters getParameters();
  }
  
  public interface Events extends Context{
    
  }
  
  public interface Outcomes extends Titled{
    /**
     * @return immutable outcomes
     */
    Map<String, Object> getOutcomes();
    
    Closure<?> getExpectClosure();
  }
  
  String getId();
  
  boolean getSuccessed();
  void setSuccessed(boolean success);
  
  Throwable getError();
  void setError(Throwable exception);
  
  Story getStory();
  
  List<? extends TestTask> getTasks();
  
  List<? extends Context> getContexts();
  
  Events getEvents();

  Outcomes getOutcomes();

}