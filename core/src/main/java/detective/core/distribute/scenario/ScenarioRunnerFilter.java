package detective.core.distribute.scenario;

import detective.core.Story;
import detective.core.StoryRunner;
import detective.core.dsl.ParametersImpl;
import detective.core.filter.RunnerFilter;
import detective.core.filter.RunnerFilterChain;
import detective.core.runner.SimpleStoryRunner;

public class ScenarioRunnerFilter implements RunnerFilter<ScenarioRunContext>{

  @Override
  public void doFilter(ScenarioRunContext context, RunnerFilterChain<ScenarioRunContext> chain) {
    Story story = context.getJobStoryRunContext().getStory();
    int scenarioIndex = story.getScenarios().indexOf(context.getScenario());
    
    if (scenarioIndex < 0)
      return; //Can't find? we don't continue
    
    int expectedScenarioIndex = context.getJobStoryRunContext().getJob().getScenarioIndex();
    if (expectedScenarioIndex != -1 && expectedScenarioIndex != scenarioIndex){
      return;
    }
    
    try {
      runScenario(context);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }
  
  private void runScenario(ScenarioRunContext context) throws Throwable{
    StoryRunner runner = new SimpleStoryRunner();
    runner.runScenario(context.getScenario(), new ParametersImpl());
  }

}
