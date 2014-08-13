package detective.core.runner;

import groovy.lang.Closure;
import groovyx.gpars.dataflow.Promise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.runtime.GroovyCategorySupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

import detective.core.Parameters;
import detective.core.Scenario;
import detective.core.Scenario.Context;
import detective.core.Story;
import detective.core.StoryRunner;
import detective.core.TestTask;
import detective.core.dsl.DslException;
import detective.core.dsl.ParametersImpl;
import detective.core.dsl.SharedDataPlaceHolder;
import detective.core.dsl.builder.DslBuilder;
import detective.core.dsl.table.Row;
import detective.core.exception.StoryFailException;
import detective.core.services.DetectiveFactory;
import detective.utils.StringUtils;

public class SimpleStoryRunner implements StoryRunner{
  
  private static final Logger logger = LoggerFactory.getLogger(SimpleStoryRunner.class); 
  
  private static Parameters aggrigateAllIncomeParameters(Story story, Scenario s){
    Parameters all = new ParametersImpl();

    Parameters shared = story.getSharedDataMap();
    all.putAllUnwrappered(shared);
    
    for (Context context : s.getContexts()){
      all.putAllUnwrappered(context.getParameters());
    }
    all.putAllUnwrappered(s.getEvents().getParameters());

    return all;
  }

  public void run(final Story story) {
    //story.setGlobalParameters(DetectiveFactory.INSTANCE.getParametersConfig());
    
    Map<Scenario, Promise<Object>> promises = new HashMap<Scenario, Promise<Object>>();
    for (final Scenario scenario : story.getScenarios()){
      try {
        
        Promise<Object> p = DetectiveFactory.INSTANCE.getThreadGroup().task(new Runnable(){

          @Override
          public void run() {
              try {
                runScenario(story, scenario);                
              } catch (Throwable e) {
                throw new StoryFailException(story, e.getMessage(), e);
              }          
          }});
        
        promises.put(scenario, p);
        
      } catch (Throwable e) {
        makeScenarioFail(scenario, e);
      }
    }
    
    for (Scenario s : promises.keySet()){
      Promise<Object> promise = promises.get(s);
      try {
        promise.join();
        if (promise.isError()){
          makeScenarioFail(s, promise.getError());
        }else{
          s.setSuccessed(true);
        }          
      } catch (Throwable e) {
        makeScenarioFail(s, e);
      }
    }
  }

  private void makeScenarioFail(Scenario s, Throwable e) {
    s.setSuccessed(false);
    s.setError(e);
    throw new StoryFailException(s.getStory(), e.getMessage(), e);
    //logger.error("Scenario [" + s.getTitle() + "] in story [" + s.getStory().getTitle() + "] fail, " + e.getMessage(), e);
  }

  private void runScenario(Story story, final Scenario scenario) throws Throwable {
    if (scenario.getTasks().size() == 0){
      throw new DslException("You need at least 1 task defined in task section, for example: scenario_1 \"scenario description\" {\n  task StockTaskFactory.stockManagerTask() \n  given \"a customer previously bought a black sweater from me\" {\n                  \n}");
    }
    
    //So far we allow one task pre-scenario, no threading
//    for (final TestTask task : scenario.getTasks()){
      final Parameters datain = aggrigateAllIncomeParameters(story, scenario);
      runScenarioWithTask(scenario, scenario.getTasks(), datain);
//    }
  }
 
  private void runScenarioWithTask(final Scenario scenario, final List<TestTask> task,
      Parameters datain) throws Throwable {
    //process all datatables
    List<Row> datatable = (List<Row>)datain.get(DslBuilder.DATATABLE_PARAMNAME);
    if (datatable != null){
      datain.remove(DslBuilder.DATATABLE_PARAMNAME);
      
      String[] headers = checkGetFirstRow(datatable);
      List<Promise<Object>> promises = new ArrayList<Promise<Object>>();
      for (int i = 1; i < datatable.size(); i++){
        Row row = datatable.get(i);
        
        prepareDataIn(datain, headers, row);
        final Parameters datainWithRow = datain.clone();
        Promise<Object> p = DetectiveFactory.INSTANCE.getThreadGroup().task(new Runnable(){
          public void run() {
            runScenario(scenario, task, datainWithRow);
          }
        });        
        promises.add(p);
      }
      
      for (Promise<Object> p : promises){
        p.join();
        if (p.isError()){
          makeScenarioFail(scenario, p.getError());
          throw p.getError();
        }
      }
    }else{
      datain = datain.clone();
      runScenario(scenario, task, datain);
    }
  }

  private Parameters prepareDataIn(Parameters datain, String[] headers, Row row) {
    Object[] values = row.asArray();
    for (int i = 0; i < headers.length; i++){
      datain.put(headers[i], values[i]);
    }
    return datain;
  }

  private String[] checkGetFirstRow(List<Row> datatable){
    
    Row headerRow = datatable.get(0);
    List<String> headers = new ArrayList<String>();
    for (Object obj : headerRow.asArray()){
      String header = obj.toString();
      if (obj instanceof PropertyToStringDelegate)
        header = ((PropertyToStringDelegate)obj).getFullPropertyName();
      
      headers.add(header);
    }
    return headers.toArray(new String[]{});
  }
  
  private void runScenario(Scenario scenario, List<TestTask> tasks, Parameters datain) {
    Parameters dataout = new ParametersImpl();
    datain = datain.clone();
    for (TestTask task : tasks){
      Parameters dataReturned = task.execute(datain);
      
      dataout.putAllUnwrappered(dataReturned);  
      datain.putAllUnwrappered(dataReturned);
      
      updateSharedData(scenario, dataout);
    }
    
    if (scenario.getOutcomes().getExpectClosure() != null){
      Closure<?> expectClosure = (Closure)scenario.getOutcomes().getExpectClosure().clone();
      
      //Shared data need join into the running user code so that they can change it
      Parameters dataToPassIntoExpectClosure = combineSharedAndInAndOut(scenario.getStory().getSharedDataMap(), datain, dataout);
      
      expectClosure.setDelegate(new ExpectClosureDelegate(dataToPassIntoExpectClosure));
      expectClosure.setResolveStrategy(Closure.DELEGATE_ONLY);
      
      try {
        //GroovyCategorySupport.use(ExpectObjectWrapper.class, scenario.getOutcomes().getExpectClosure());
        expectClosure.call();
      } catch (WrongPropertyNameInDslException e) {
        StringBuilder sb = new StringBuilder(e.getPropertyName());
        sb.append(" not able to found in properties list, do you mean : ")
        .append(StringUtils.getBestMatch(e.getPropertyName(), dataToPassIntoExpectClosure.keySet()).or("notAbleToFoundBestMatchProperties"))
        .append("\nthe avaiable properties we got:")
        .append(dataToPassIntoExpectClosure.keySet())
        .append("\nHere is the properties and the values for your reference:\n")
        .append(dataToPassIntoExpectClosure.toString());
        throw new DslException(sb.toString(), e);
      } catch (java.lang.AssertionError e){
        throw new detective.core.AssertionError(scenario.getStory(), scenario, scenario.getOutcomes(), e);
      }
    }else{
      throw new DslException("There is no \"then\" section in DSL scenario part. \n " + scenario.toString());
    }
  }

  private void updateSharedData(Scenario scenario, Parameters dataout) {
    Story story = scenario.getStory();
    Set<String> sharedDataKeys = story.getSharedDataMap().keySet();
    for (String key : sharedDataKeys){
      if (dataout.containsKey(key))
       story.putSharedData(key, dataout.get(key));
    }
  }
  
  /**
   * Dataout will override data in if have same key
   * @param datain
   * @param dataout
   * @return
   */
  private Parameters combineSharedAndInAndOut(Parameters shared, Parameters datain, Parameters dataout){
    Parameters p = new ParametersImpl(shared);
    p.putAllUnwrappered(datain);
    p.putAllUnwrappered(dataout);
    return p;
  }
  
//  private Set<String> getPlaceHolderKeys(Story story){
//    Set<String> keys = new HashSet<String>();
//    Parameters parameters = story.getSharedDataMap();
//    for (String key : parameters.keySet()){
//      Object value = parameters.get(key);
//      if (value != null && value.equals(SharedDataPlaceHolder.INSTANCE)){
//        keys.add(key);
//      }
//    }
//    return keys;
//  }

}
