package detective.core;

import detective.core.Scenario.Step;
import detective.core.exception.StoryFailException;

public class AssertionError extends StoryFailException {

  private static final long serialVersionUID = 1L;

  private final Scenario scenario;
  private final Step outcomes;
  
  public AssertionError(Story story, Scenario scenario, Step step, java.lang.AssertionError cause) {
    super(story, cause.getMessage() + "\n scenario [" + scenario.getTitle() + "] step [" + step.getTitle() + "] in story [" + story.getTitle() + "] ", cause);
    
    this.scenario = scenario;
    this.outcomes = step;
  }

  public Step getOutcomes() {
    return outcomes;
  }

  public Scenario getScenario() {
    return scenario;
  }

  
  
}
