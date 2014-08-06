package detective.core;

import detective.core.Scenario.Outcomes;
import detective.core.exception.StoryFailException;

public class AssertionError extends StoryFailException {

  private static final long serialVersionUID = 1L;

  private final Scenario scenario;
  private final Outcomes outcomes;
  
  public AssertionError(Story story, Scenario scenario, Outcomes outcomes, java.lang.AssertionError cause) {
    super(story, cause.getMessage() + "\n scenario [" + scenario.getTitle() + "] then [" + outcomes.getTitle() + "] in story [" + story.getTitle() + "] ", cause);
    
    this.scenario = scenario;
    this.outcomes = outcomes;
  }

  public Outcomes getOutcomes() {
    return outcomes;
  }

  public Scenario getScenario() {
    return scenario;
  }

  
  
}
