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
import com.typesafe.config.ConfigException;

import detective.core.Parameters;
import detective.core.Scenario;
import detective.core.Scenario.Step;
import detective.core.Detective;
import detective.core.Story;
import detective.core.StoryRunner;
import detective.core.TestTask;
import detective.core.distribute.Job;
import detective.core.dsl.DslException;
import detective.core.dsl.ParametersImpl;
import detective.core.dsl.SharedDataPlaceHolder;
import detective.core.dsl.builder.DslBuilder;
import detective.core.dsl.table.Row;
import detective.core.exception.StoryFailException;
import detective.core.filter.RunnerFilterChain;
import detective.core.services.DetectiveFactory;
import detective.utils.StringUtils;

public class SimpleStoryRunner implements StoryRunner{
  
  private static final Logger logger = LoggerFactory.getLogger(SimpleStoryRunner.class); 
  
  public SimpleStoryRunner(){
  }

  public void run(final Story story, final Job job) {
    int scenarioIndexConfig = getScenarioIndexConfig();
    
    Map<Scenario, Promise<Object>> promises = new HashMap<Scenario, Promise<Object>>();
    int currentIndex = 0;
    for (final Scenario scenario : story.getScenarios()){
      try {
        if (scenarioIndexConfig == -1 || scenarioIndexConfig == currentIndex){
          Promise<Object> p = DetectiveFactory.INSTANCE.getThreadGroup().task(new Runnable(){
            @Override
            public void run() {
                try {
                  runScenario(scenario, new ParametersImpl());
                } catch (Throwable e) {
                  throw new StoryFailException(story, e.getMessage(), e);
                }          
            }});
          promises.put(scenario, p);
        }else{
          scenario.setIgnored(true);
        }
        
        currentIndex ++;
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

  private int getScenarioIndexConfig() throws ConfigException.WrongType{
    int scenarioIndex = -1;
    try {
      scenarioIndex = Detective.getConfig().getNumber("detective.runner.scenario.index").intValue();
    } catch (ConfigException.Missing e1) {
      
    }
    return scenarioIndex;
  }

  private void makeScenarioFail(Scenario s, Throwable e) {
    s.setSuccessed(false);
    s.setError(e);
    throw new StoryFailException(s.getStory(), e.getMessage(), e);
    //logger.error("Scenario [" + s.getTitle() + "] in story [" + s.getStory().getTitle() + "] fail, " + e.getMessage(), e);
  }
  
  //Handle Scenario Role
  public void runScenario(final Scenario scenario, Parameters config) throws Throwable{
    Parameters datain = new ParametersImpl(config);
    
    List<Row> datatable = scenario.getScenarioTable();
    if (datatable != null && datatable.size() >= 1){
      String[] headers = checkGetHeader(datatable);
      List<Promise<Object>> promises = new ArrayList<Promise<Object>>();
      for (int i = 0; i < datatable.size(); i++){
        Row row = datatable.get(i);
        
        prepareDataIn(datain, headers, row);
        final Parameters datainWithRow = datain.clone();
        Promise<Object> p = DetectiveFactory.INSTANCE.getThreadGroup().task(new Runnable(){
          public void run() {
            runScenarioNew(scenario, datainWithRow);
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
      runScenarioNew(scenario, datain);
    }
  }
  
  public void runScenarioNew(final Scenario scenario, Parameters datain) {
    //Shared data need join into the running user code so that they can change it
    Parameters parameterForWholeScenario = new ParametersImpl(scenario.getStory().getSharedDataMap());
    parameterForWholeScenario.putAll(datain);
    
    for (Step step : scenario.getSteps()){
      if (step.getExpectClosure() != null){
        Closure<?> expectClosure = (Closure)step.getExpectClosure().clone();
        
        Parameters dataToPassIntoExpectClosure = parameterForWholeScenario;
        
        expectClosure.setDelegate(new ExpectClosureDelegate(dataToPassIntoExpectClosure));
        //expectClosure.setResolveStrategy(Closure.DELEGATE_ONLY);
        expectClosure.setResolveStrategy(Closure.DELEGATE_FIRST);
        
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
          throw new detective.core.AssertionError(scenario.getStory(), scenario, step, e);
        } catch (groovy.lang.MissingPropertyException e){
          throw new DslException(e.getMessage() +  ". Please note we have a know ambiguousness for parent child relationship, for example login.username is a valid identifier for us, but when you add login.username.lastname, we have no idea it is going to access a property from identifier login.username or it is a new identifier.", e);
        }
        
        //this.updateSharedData(scenario, dataToPassIntoExpectClosure);
      }else{
        throw new DslException("There is no \"then\" section in DSL scenario part. \n " + scenario.toString());
      }
    }
  }

  private Parameters prepareDataIn(Parameters datain, String[] headers, Row row) {
    Object[] values = row.asArray();
    for (int i = 0; i < headers.length; i++){
      datain.put(headers[i], values[i]);
    }
    return datain;
  }

  private String[] checkGetHeader(List<Row> datatable){
    return datatable.get(0).getHeaderAsStrings();
  }

  private void updateSharedData(Scenario scenario, Parameters dataout) {
    Story story = scenario.getStory();
    Set<String> sharedDataKeys = story.getSharedDataMap().keySet();
    Set<String> unbindShareVarKeys = dataout.getUnbindShareVarKeys();
    for (String key : sharedDataKeys){
      if (!unbindShareVarKeys.contains(key) && dataout.containsKey(key))
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

}
