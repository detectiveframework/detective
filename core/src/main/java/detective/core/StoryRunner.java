package detective.core;

import detective.core.distribute.JobToRun;

public interface StoryRunner {

  void run(Story story, final JobToRun job);
  
  void runScenario(final Scenario scenario, Parameters config);
  
  
  
}
