package detective.core.distribute;

import groovy.lang.Script;

import java.util.ArrayList;

import detective.common.ClassUtils;
import detective.core.Detective;
import detective.core.Scenario;
import detective.core.Story;
import detective.core.runner.DslBuilderAndRun;
import detective.core.runner.RunnerInterceptor;
import detective.core.runner.SimpleStoryRunner;

/**
 * Please be very careful, this class will change DslBuilderAndRun behavior to run storys
 * in spark not the normal process 
 * 
 * @author James Luo
 *
 */
public class SparkJobDistributor implements JobDistributor {
  
  class SparkRunnerInterceptor implements RunnerInterceptor{

    @Override
    public boolean beforeRun(Story story) {
      try {
        distribute(story);
      } catch (Exception e) {
        Detective.error("Error to distribute story [" + story.getTitle() + "]" +  e.getMessage(), e);
      }
      return false;
    }

    @Override
    public void afterRun(Story story) {
      
    }
    
  } 

  @Override
  public void distribute(Story story) {
    if (story.getSharedDataMap().size() > 0){
      try {
        new SimpleStoryRunner().run(story);
      } catch (Throwable e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } 
    }else{
      for (Scenario scenario : story.getScenarios()){
        try {
          new SimpleStoryRunner().runScenario(scenario);
        } catch (Throwable e) {
          e.printStackTrace();
        } 
      }
    }
  }
  
  public void distributeAll(String packageName){
    try {
      DslBuilderAndRun.setInterceptor(new SparkRunnerInterceptor());
      
      ArrayList<Class<?>> classes = ClassUtils.getClassesForPackage(packageName);
      for (Class<?> clazz : classes){
        if (clazz.isAssignableFrom(Script.class)){
          Script script = (Script)clazz.newInstance();
          script.run();
        }
      }
    } catch (Exception e) {
      Detective.error(e.getMessage(), e);
    } finally{
      DslBuilderAndRun.setInterceptor(null);
    }
  }

}
