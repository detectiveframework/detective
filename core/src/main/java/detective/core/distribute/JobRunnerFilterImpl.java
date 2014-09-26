package detective.core.distribute;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import groovy.lang.Script;
import detective.core.Story;
import detective.core.filter.FilterChainFactory;
import detective.core.filter.RunnerFilter;
import detective.core.filter.RunnerFilterChain;
import detective.core.filter.impl.RunnerFilterChainImpl;
import detective.core.runner.DslBuilderAndRun;

public class JobRunnerFilterImpl implements JobRunner {
  
  class StoryFilterChainAdapter implements RunnerFilterChain<Story>{
    private final Job job;
    private final RunnerFilterChain<JobStoryRunContext> jobStoryChain;
    
    private final RunnerFilterChain<Story> internalChain;
    
    private int storyPositionIndex = -1;
    
    public StoryFilterChainAdapter(Job job, RunnerFilterChain<JobStoryRunContext> chain){
      this.job = job;
      this.jobStoryChain = chain;
      this.internalChain = new RunnerFilterChainImpl<Story>(createFilters());
    }
    
    private List<RunnerFilter<Story>> createFilters(){
      List<RunnerFilter<Story>> filters = new ArrayList<RunnerFilter<Story>>();
      filters.add(new RunnerFilter<Story>(){
        @Override
        public void doFilter(Story t, RunnerFilterChain<Story> chain) {
          JobStoryRunContext context = new JobStoryRunContext();
          context.setStory(t);
          context.setJob(job);
          context.setCurrentStoryIndex(increaseStoryPositionAndGet());
          context.setScriptClassName(job.getStoryClassName());
          jobStoryChain.doFilter(context);
        }});
      return filters;
    }
    
    private int increaseStoryPositionAndGet(){
      storyPositionIndex ++ ;
      return storyPositionIndex;
    }

    @Override
    public Iterator<RunnerFilter<Story>> iterator() {
      return this.internalChain.iterator();
    }

    @Override
    public void doFilter(Story t) {
      this.internalChain.doFilter(t);
    }

    @Override
    public void resetChainPosition() {
      this.internalChain.resetChainPosition();
    }

  } 

  @Override
  public void run(Job job) {
    RunnerFilterChain<JobStoryRunContext> chain = createFilterChain();
    StoryFilterChainAdapter adapter = new StoryFilterChainAdapter(job, chain);
    DslBuilderAndRun.setFilterChainCurrentThread(adapter);
    try {
      Class<?> clazz = Class.forName(job.getStoryClassName());
      
      if (! (clazz.getSuperclass().getName().equals("groovy.lang.Script"))){
        throw new RuntimeException(job.getStoryClassName() + " is not a groovy script");
      }
      
      Script script = (Script)clazz.newInstance();
      script.run();
      
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @SuppressWarnings("unchecked")
  private RunnerFilterChain<JobStoryRunContext> createFilterChain(){
    try {
      FilterChainFactory factory = FilterChainFactory.ConfigReader.instanceFromConfigFile("runner.spark_running.filter_chain_factory");
      return (RunnerFilterChain<JobStoryRunContext>)factory.getChain();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
