package detective.core.distribute.collector;

import groovy.lang.Script;

import java.util.ArrayList;
import java.util.List;

import detective.common.ClassUtils;
import detective.core.Detective;
import detective.core.Scenario;
import detective.core.Story;
import detective.core.distribute.Job;
import detective.core.filter.FilterChainFactory;
import detective.core.filter.RunnerFilter;
import detective.core.filter.RunnerFilterChain;
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
public class JobCollector {
  
  private FilterChainFactory chainFactory;
  
  private JobCollector(){
    try {
      chainFactory = FilterChainFactory.ConfigReader.instanceFromConfigFile("runner.spark_distribute.filter_chain_factory");
    } catch (Exception e) {
      throw new RuntimeException(e);
    } 
    DslBuilderAndRun.setFilterChainCurrentThread((RunnerFilterChain<Story>) chainFactory.getChain());
  }
  
  private List<Job> getJobs(){
   for (RunnerFilter<?> filter : chainFactory.getChain()){
     if (filter instanceof JobCollectorFilter){
       return ((JobCollectorFilter)filter).getJobs();
     }
   } 
   
   throw new RuntimeException("No detective.core.distribute.JobCollectorFilter found in filter chain : " + chainFactory.getClass().getName());
  }
  
  
  private void shutdown(){
    DslBuilderAndRun.setFilterChainCurrentThread(null);
  }
  
  public static List<Job> collectAll(String packageName){
    List<Job> jobs = new ArrayList<Job>();
    
    try {
      ArrayList<Class<?>> classes = ClassUtils.getClassesForPackage(packageName);
      for (Class<?> clazz : classes){
        //if (clazz.isAssignableFrom(Script.class)){ //not work for some reason
        if (clazz.getSuperclass().getName().equals("groovy.lang.Script")){
          jobs.addAll(collectJobs(clazz));
        }
      }
    } catch (Exception e) {
      Detective.error(e.getMessage(), e);
    }
    
    return jobs;
  }

  private static List<Job> collectJobs(Class<?> clazz) throws InstantiationException,
      IllegalAccessException {
    JobCollector dis = new JobCollector();
    try{
      Script script = (Script)clazz.newInstance();
      script.run();
      
      for (Job job : dis.getJobs())
        job.setStoryClassName(clazz.getName());
    }finally{
      dis.shutdown();
    }
    return dis.getJobs();
  }

}
