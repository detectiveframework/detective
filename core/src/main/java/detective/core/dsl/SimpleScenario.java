package detective.core.dsl;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import detective.core.Scenario;
import detective.core.Story;
import detective.core.TestTask;
import detective.core.Scenario.Context;
import detective.core.Scenario.Events;
import detective.core.Scenario.Outcomes;
import groovy.lang.GroovyObjectSupport;

/**
 * <pre>
Scenario 1: Title
Given [context]
  And [some more context]...
When  [event]
Then  [outcome]
  And [another outcome]...</pre>
  
 * @author James Luo
 *
 */
public class SimpleScenario extends GroovyObjectSupport implements Scenario{
  
  private String id;
  private Boolean successed = false;
  private Throwable error;
  private final String title;
  private final Story story;
  private final List<SimpleContext> contexts = new ArrayList<SimpleContext>();
  private final SimpleEvents events;
  private final SimpleOutcomes outcomes = new SimpleOutcomes();
  private final List<TestTask> tasks = new ArrayList<TestTask>();
  
  private boolean isImmutable = false;

  public SimpleScenario(Story story, String title){
    this.story = story;
    events = new SimpleEvents(this);
    this.title = title;
  }

  @Override
  public String toString() {
    return id + " \"" + title + "\"\n   tasks=" + tasks + ",\n   give " + contexts
        + "}\n   when " + events + "\n   then " + outcomes + "";
  }

  public Story getStory() {
    return story;
  }

  public String getTitle() {
    return title;
  }

  public List<? extends Context> getContexts() {
    return ImmutableList.copyOf(this.contexts);
  }
  
  public void addContext(SimpleContext context){
    checkImmutable();
    
    this.contexts.add(context);
  }

  private void checkImmutable() {
    if (isImmutable)
      throw new DslException("Scenario is immuable.");
  }

  public Events getEvents() {
    return this.events;
  }

  public Outcomes getOutcomes() {
    return this.outcomes;
  }
  
  public List<TestTask> getTasks() {
    return ImmutableList.copyOf(tasks);
  }
  
  public SimpleScenario addTask(TestTask task){
    checkImmutable();
    
    if (this.tasks.size() >= 1)
      throw new DslException("We support only one task pre scenario at this moment.");
    
    this.tasks.add(task);
    return this;
  }
  
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    checkImmutable();
    
    this.id = id;
  }

  @Override
  public boolean getSuccessed() {
    return successed;
  }

  @Override
  public void setSuccessed(boolean success) {
    successed = success;
  }

  @Override
  public Throwable getError() {
    return error;
  }

  @Override
  public void setError(Throwable exception) {
    this.error = exception;
  }


  
}
