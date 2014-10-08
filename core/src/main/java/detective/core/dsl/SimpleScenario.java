package detective.core.dsl;

import groovy.lang.GroovyObjectSupport;

import java.util.ArrayList;
import java.util.List;

import detective.core.Scenario;
import detective.core.Story;
import detective.core.dsl.table.Row;

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
  
  private final List<Step> steps = new ArrayList<Step>();
  
  private final List<Row> scenarioTable = new ArrayList<Row>();
  
  private boolean isImmutable = false;

  public SimpleScenario(Story story, String title){
    this.story = story;
    this.title = title;
  }

  

  @Override
  public String toString() {
    return "SimpleScenario [id=" + id + ", title=" + title + ", scenarioTable=" + scenarioTable
        + ", steps=" + steps + "]";
  }

  public Story getStory() {
    return story;
  }

  public String getTitle() {
    return title;
  }
  
  private void checkImmutable() {
    if (isImmutable)
      throw new DslException("Scenario is immuable.");
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

  @Override
  public List<Step> getSteps() {
    return steps;
  }

  public void addStep(Step step){
    steps.add(step);
  }

  @Override
  public List<Row> getScenarioTable() {
    return scenarioTable;
  }
  
}
