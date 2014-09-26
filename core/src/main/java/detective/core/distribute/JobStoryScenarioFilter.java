package detective.core.distribute;

import detective.core.Scenario;
import detective.core.distribute.scenario.ScenarioRunContext;
import detective.core.filter.RunnerFilter;
import detective.core.filter.RunnerFilterChain;

/**
 * Split Story into scenarios and invoke scenarios into ScenarioRunnerFilters
 * 
 * @author James Luo
 *
 */
public class JobStoryScenarioFilter implements RunnerFilter<JobStoryRunContext>{
  
  private final RunnerFilterChain<ScenarioRunContext> scenarioChain;
  
  public JobStoryScenarioFilter(RunnerFilterChain<ScenarioRunContext> chain){
    assert chain != null;
    
    this.scenarioChain = chain;
  }

  @Override
  public void doFilter(JobStoryRunContext t, RunnerFilterChain<JobStoryRunContext> chain) {
    for (final Scenario scenario : t.getStory().getScenarios()){
      ScenarioRunContext context = new ScenarioRunContext();
      context.setJobStoryRunContext(t);
      context.setScenario(scenario);
      
      this.scenarioChain.doFilter(context);
    }
    
    chain.doFilter(t);
  }

}
