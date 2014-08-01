package detective.core.dsl;

import detective.core.Scenario;
import detective.core.Scenario.Events;

public class SimpleEvents extends SimpleContext implements Events{

  public SimpleEvents(Scenario scenario) {
    super(scenario);
  }
  
}