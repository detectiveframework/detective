package detective.core;

import groovy.lang.Closure;

import java.util.List;
import java.util.Map;

public interface Scenario extends Titled{
  
  public interface Context extends Titled{
    /**
     * @return immutable map for parameters;
     */
    Map<String, Object> getParameters();
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
  
  Story getStory();
  
  List<? extends TestTask> getTasks();
  
  List<? extends Context> getContexts();
  
  Events getEvents();

  Outcomes getOutcomes();

}