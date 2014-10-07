package detective.core.distribute.collector;

import java.util.ArrayList;
import java.util.List;

import detective.core.Story;
import detective.core.filter.FilterChainFactory;
import detective.core.filter.RunnerFilter;
import detective.core.filter.RunnerFilterChain;
import detective.core.filter.console.LogToConsoleFilter;
import detective.core.filter.impl.RunnerFilterChainImpl;
import detective.core.filter.runner.SimpleStoryRunnerFilter;

public class JobCollectFilterChainFactory implements FilterChainFactory{

  private final RunnerFilterChain<Story> chain;
  
  public JobCollectFilterChainFactory(){
    List<RunnerFilter<Story>> filters = new ArrayList<RunnerFilter<Story>>(); 
    filters.add(new JobCollectorFilter());
    chain = new RunnerFilterChainImpl<Story>(filters);
  }
  
  @Override
  public RunnerFilterChain<Story> getChain() {
    return chain;
  }

}
