package detective.core.distribute;

import java.util.ArrayList;
import java.util.List;

import detective.core.Story;
import detective.core.distribute.scenario.ScenarioRunContext;
import detective.core.distribute.scenario.ScenarioRunnerFilter;
import detective.core.filter.FilterChainFactory;
import detective.core.filter.RunnerFilter;
import detective.core.filter.RunnerFilterChain;
import detective.core.filter.console.LogToConsoleFilter;
import detective.core.filter.impl.RunnerFilterChainImpl;
import detective.core.filter.runner.SimpleStoryRunnerFilter;

public class SparkJobRunnerFilterChainFactory implements FilterChainFactory{

  private final RunnerFilterChain<JobStoryRunContext> chain;
  
  public SparkJobRunnerFilterChainFactory(){
    List<RunnerFilter<JobStoryRunContext>> filters = new ArrayList<RunnerFilter<JobStoryRunContext>>(); 
    filters.add(new JobStoryFilter());
    filters.add(new JobStoryScenarioFilter(createScenarioRunnerChain()));
    chain = new RunnerFilterChainImpl<JobStoryRunContext>(filters);
  }
  
  @Override
  public RunnerFilterChain<JobStoryRunContext> getChain() {
    return chain;
  }
  
  private RunnerFilterChain<ScenarioRunContext> createScenarioRunnerChain(){
    List<RunnerFilter<ScenarioRunContext>> filters = new ArrayList<RunnerFilter<ScenarioRunContext>>(); 
    filters.add(new ScenarioRunnerFilter());
    return new RunnerFilterChainImpl<ScenarioRunContext>(filters);
  }

}
