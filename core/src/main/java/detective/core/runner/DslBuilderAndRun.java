package detective.core.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import detective.core.Scenario;
import detective.core.Story;
import detective.core.dsl.builder.DslBuilder;
import detective.core.exception.StoryFailException;
import detective.core.filter.FilterChainFactory;
import detective.core.filter.RunnerFilter;
import detective.core.filter.RunnerFilterChain;
import detective.core.services.DetectiveFactory;

public class DslBuilderAndRun extends DslBuilder {
  
  //TODO James Global Variable here, refactor when have time
  private static ThreadLocal<RunnerFilterChain<Story>> filterChainCurrentThread = new ThreadLocal<RunnerFilterChain<Story>>();
  
  /**
   * Setup chain for current thread
   *
   */
  public static void setFilterChainCurrentThread(RunnerFilterChain<Story> chain){
    filterChainCurrentThread.set(chain);
  }
  
  private static final Logger logger = LoggerFactory.getLogger(DslBuilderAndRun.class);
  
  protected Object doInvokeMethod(String methodName, Object name, Object args){
    Object obj = super.doInvokeMethod(methodName, name, args);
    if (obj instanceof Story){
      Story story = (Story)obj;
      if (story.getTitle() != null && story.getTitle().equals(methodName)){
        doFinishedBuilding(story);
      }
    }
    return obj;
  }
  
  @SuppressWarnings("unchecked")
  protected Object doFinishedBuilding(Story story){
    try {
      if (filterChainCurrentThread.get() == null){
        setFilterChainCurrentThread((RunnerFilterChain<Story>)FilterChainFactory.ConfigReader.instanceFromConfigFile("runner.normal.filter_chain_factory").getChain());
      }
      
      if (filterChainCurrentThread.get() != null){
        RunnerFilterChain<Story> chain = filterChainCurrentThread.get();
        chain.resetChainPosition();
        chain.doFilter(story);        
      }
    }catch (Exception e){
      if (e instanceof StoryFailException)
        throw (StoryFailException)e;
      else
        throw new StoryFailException(story, e.getMessage(), e);
    }
      
    return story;
  }

}
