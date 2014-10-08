package detective.core.dsl;

import groovy.lang.Closure;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import detective.core.Scenario.Step;

public class SimpleStep implements Step{
  private String title;
  private Closure<?> expectClosure;
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    return "\"" + title + "\"\n";
  }

  public void setExpectClosure(Closure<?> expectClosure) {
    this.expectClosure = expectClosure;
  }

  public Closure<?> getExpectClosure() {
    return this.expectClosure;
  }
  
}