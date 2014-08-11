package detective.core;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import groovy.lang.Closure;

import org.hamcrest.Matcher;

import detective.core.dsl.builder.DslBuilder;
import detective.core.matcher.IsEqual;
import detective.core.runner.DslBuilderAndRun;
import detective.task.EchoTask;
import detective.task.HttpClientTask;
import detective.utils.StringUtils;

public class Detective {

  public static DslBuilder story(){
    return new DslBuilderAndRun();
  }
  
  public static DslBuilder feature(){
    return new DslBuilderAndRun();
  }
  
  public static JsonBuilder jsonBuilder(){
    return new JsonBuilder();
  }
  
  public static JsonBuilder jsonBuilder(Closure c){
    JsonBuilder builder = new JsonBuilder();
    builder.call(c);
    return builder;
  }
  
  public static Object parseJson(String json){
    return (new JsonSlurper()).parseText(json);
  }
  
  public static EchoTask echoTask(){
    return new EchoTask();
  }
  
  public static HttpClientTask httpclientTask(){
    return new HttpClientTask();
  }
  
  //Matches ==============
  public static <T> Matcher<T> equalTo(T operand) {
    return IsEqual.equalTo(operand);
  }
  
  //Utilities
  public static String randomId() {
    return StringUtils.randomBase64UUID();
  }
  
}
