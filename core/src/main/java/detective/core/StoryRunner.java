package detective.core;

import detective.core.distribute.Job;

public interface StoryRunner {

  void run(Story story, final Job job);
  
  void runScenario(final Scenario scenario) throws Throwable;
  
  
  
}
