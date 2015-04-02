package detective.core.distribute.scenario;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import detective.core.Parameters;
import detective.core.Scenario;
import detective.core.Story;
import detective.core.StoryRunner;
import detective.core.distribute.JobRunResult;
import detective.core.distribute.JobToRun;
import detective.core.dsl.ParametersImpl;
import detective.core.filter.RunnerFilter;
import detective.core.filter.RunnerFilterChain;
import detective.core.runner.SimpleStoryRunner;

public class ScenarioRunnerFilter implements RunnerFilter<ScenarioRunContext>{
  
  private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioRunnerFilter.class);

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
      //We don't throw exception otherwise Spark will keep trying
      LOGGER.error(e.getMessage(), e);
    }
    
    setupJobResult(context);
  }
  
  private void runScenario(ScenarioRunContext context) throws Throwable{
    StoryRunner runner = new SimpleStoryRunner();
    Parameters params = new ParametersImpl();
    params.put("_scenarioContext", context);
    runner.runScenario(context.getScenario(), params);
  }
  
  private void setupJobResult(ScenarioRunContext context){
    Scenario scenario = context.getScenario();
    JobToRun job = context.getJobStoryRunContext().getJob();
    JobRunResult result = new JobRunResult();
    result.setStoryName(scenario.getStory().getTitle());
    result.setScenarioName(scenario.getTitle());
    result.setSuccessed(scenario.getSuccessed());
    result.setIgnored(scenario.getIgnored());
    result.setError(scenario.getError());
    result.getSteps().addAll(context.getSteps());
    job.setJobResult(result);
  }

}
