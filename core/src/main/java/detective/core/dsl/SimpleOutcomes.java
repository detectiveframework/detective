package detective.core.dsl;

import groovy.lang.Closure;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import detective.core.Scenario.Outcomes;

public class SimpleOutcomes implements Outcomes{
  private Map<String, Object> outcomes = new HashMap<String, Object>(); 
  private String title;
  private Closure<?> expectClosure;
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Map<String, Object> getOutcomes() {
    return ImmutableMap.copyOf(outcomes);
  }
  
  public void addOutcomes(String key, Object value){
    this.outcomes.put(key, value);
  }

  @Override
  public String toString() {
    return "\"" + title + "\" {\n      outcomes {" + outcomes + "}\n      }";
  }

  public void setExpectClosure(Closure<?> expectClosure) {
    this.expectClosure = expectClosure;
  }

  public Closure<?> getExpectClosure() {
    return this.expectClosure;
  }
  
}