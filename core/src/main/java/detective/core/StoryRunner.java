package detective.core;

public interface StoryRunner {

  void run(Story story);
  
  void runScenario(final Scenario scenario) throws Throwable;
  
  
  
}
