package detective.core.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import detective.core.Scenario;
import detective.core.Story;
import detective.core.dsl.builder.DslBuilder;
import detective.core.exception.StoryFailException;

public class DslBuilderAndRun extends DslBuilder {
  
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
  
  protected Object doFinishedBuilding(Story story){
    try {
      new SimpleStoryRunner().run(story);
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
        throw new StoryFailException(story, error.getMessage(), error);
      else
        logger.info("Story [" + story.getTitle() + "] ran successfully.");
    } catch (StoryFailException e) {
      throw e;
    } catch (Throwable e){
      throw new StoryFailException(story, e.getMessage(), e);
    }
    
    
    return story;
  }

}
