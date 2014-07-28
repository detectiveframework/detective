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

import com.google.common.collect.ImmutableMap;

import detective.core.Scenario;
import detective.core.Scenario.Context;
import detective.core.Story;
import detective.core.StoryRunner;
import detective.core.TestTask;
import detective.core.dsl.DslException;
import detective.core.dsl.SharedDataPlaceHolder;
import detective.core.dsl.builder.DslBuilder;
import detective.core.dsl.table.Row;
import detective.core.services.DetectiveFactory;
import detective.utils.StringUtils;

public class SimpleStoryRunner implements StoryRunner{
  
  private static Map<String, Object> aggrigateAllIncomeParameters(Story story, Scenario s){
    Map<String, Object> all = new HashMap<String, Object>();

    Map<String, Object> shared = story.getSharedDataMap();
    all.putAll(shared);
    
    for (Context context : s.getContexts()){
      all.putAll(context.getParameters());
    }
    all.putAll(s.getEvents().getParameters());

    return all;
  }

  public void run(Story story) {
    for (Scenario scenario : story.getScenarios()){
      if (scenario.getTasks().size() == 0){
        throw new DslException("You need at least 1 task defined in task section, for example: scenario_1 \"scenario description\" {\n  task StockTaskFactory.stockManagerTask() \n  given \"a customer previously bought a black sweater from me\" {\n                  \n}");
      }
      
      for (TestTask task : scenario.getTasks()){
        final Map<String, Object> datain = aggrigateAllIncomeParameters(story, scenario);
        
        try {
          runScenarioWithTask(scenario, task, datain);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void runScenarioWithTask(final Scenario scenario, final TestTask task,
      Map<String, Object> datain) throws InterruptedException {
    //process all datatables
    List<Row> datatable = (List<Row>)datain.get(DslBuilder.DATATABLE_PARAMNAME);
    if (datatable != null){
      datain.remove(DslBuilder.DATATABLE_PARAMNAME);
      
      String[] headers = checkGetFirstRow(datatable);
      List<Promise<Object>> promises = new ArrayList<Promise<Object>>();
      for (int i = 1; i < datatable.size(); i++){
        Row row = datatable.get(i);
        
        prepareDataIn(datain, headers, row);
        final Map<String, Object> datainWithRow = ImmutableMap.copyOf(datain);
        Promise<Object> p = DetectiveFactory.INSTANCE.getThreadGroup().task(new Runnable(){
          public void run() {
            runScenario(scenario, task, datainWithRow);
          }
        });        
        promises.add(p);
      }
      
      for (Promise<Object> p : promises)
        p.join();
    }else{
      runScenario(scenario, task, datain);
    }
  }

  private Map<String, Object> prepareDataIn(Map<String, Object> datain, String[] headers, Row row) {
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
  
  private void runScenario(Scenario scenario, TestTask task, Map<String, Object> datain) {
    datain = ImmutableMap.copyOf(datain);
    
    Map<String, Object> dataout = task.execute(datain);        
    dataout = combineInAndOut(datain, dataout);
    
    updateSharedData(scenario, dataout);
    
    if (scenario.getOutcomes().getExpectClosure() != null){
      scenario.getOutcomes().getExpectClosure().setDelegate(new ExpectClosureDelegate(dataout));
      scenario.getOutcomes().getExpectClosure().setResolveStrategy(Closure.DELEGATE_ONLY);
      
      try {
        GroovyCategorySupport.use(ExpectObjectWrapper.class, scenario.getOutcomes().getExpectClosure());
        //scenario.getOutcomes().getExpectClosure().call();
      } catch (WrongPropertyNameInDslException e) {
        StringBuilder sb = new StringBuilder(e.getPropertyName());
        sb.append(" not able to found in properties list, do you mean : ")
        .append(StringUtils.getBestMatch(e.getPropertyName(), dataout.keySet()).or("notAbleToFoundBestMatchProperties"))
        .append("\nthe avaiable properties we got:")
        .append(dataout.keySet())
        .append("\nHere is the properties and the values for your reference:\n")
        .append(dataout.toString());
        throw new DslException(sb.toString());
      }
    }else{
      throw new DslException("There is no \"then\" section in DSL scenario part. \n " + scenario.toString());
    }
  }

  private void updateSharedData(Scenario scenario, Map<String, Object> dataout) {
    Story story = scenario.getStory();
    Set<String> sharedDataKeys = this.getPlaceHolderKeys(story);
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
  private Map<String, Object> combineInAndOut(Map<String, Object> datain, Map<String, Object> dataout){
    Map<String, Object> combined = new HashMap<String, Object>(datain);
    combined.putAll(dataout);
    return ImmutableMap.copyOf(combined);
//    return combined;
  }
  
  private Set<String> getPlaceHolderKeys(Story story){
    Set<String> keys = new HashSet<String>();
    Map<String, Object> parameters = story.getSharedDataMap();
    for (String key : parameters.keySet()){
      Object value = parameters.get(key);
      if (value != null && value.equals(SharedDataPlaceHolder.INSTANCE)){
        keys.add(key);
      }
    }
    return keys;
  }

}
