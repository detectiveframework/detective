package detective.core.distribute.scenario;

import detective.core.Story;
import detective.core.filter.RunnerFilter;
import detective.core.filter.RunnerFilterChain;

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
      //runScenario(context.getScenario());
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

}
