package detective.core.exception;

import detective.core.Scenario;

public class ScenarioFailException extends StoryFailException {
  
  private static final long serialVersionUID = 1L;
  
  private final transient Scenario scenario;

  public ScenarioFailException(Scenario scenario, int precentCompleted, String message, Throwable cause) {
    super(scenario.getStory(), precentCompleted, message, cause);
    this.scenario = scenario;
  }

  public Scenario getScenario() {
    return scenario;
  }

}
