package detective.core.filter.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import detective.core.Scenario;
import detective.core.Story;
import detective.core.exception.StoryFailException;
import detective.core.filter.RunnerFilter;
import detective.core.filter.RunnerFilterChain;

public class LogToConsoleFilter implements RunnerFilter<Story> {
  
  private static final Logger logger = LoggerFactory.getLogger(LogToConsoleFilter.class);

  @Override
  public void doFilter(Story story, RunnerFilterChain<Story> chain) {
    try {
      boolean storyFailed = false;
      Throwable error  = null;
      for (Scenario s : story.getScenarios()){
        if (!s.getSuccessed()){
          storyFailed = true;
          error = s.getError();
          logger.error(error.getMessage(), error);
        }
      }
      if (storyFailed && error != null)
        throw error;
      else
        logger.info("Story [" + story.getTitle() + "] ran successfully.");
    } catch (Throwable e){
      logger.error("Story [" + story.getTitle() + "] error." + e.getMessage(), e);
      if (e instanceof StoryFailException)
        throw (StoryFailException)e;
      else
        throw new StoryFailException(story, e.getMessage(), e);
    }
    
    chain.doFilter(story);
  }

}
