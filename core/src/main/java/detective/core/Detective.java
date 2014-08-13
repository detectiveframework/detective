package detective.core;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import groovy.lang.Closure;
import groovy.util.XmlSlurper;
import groovy.xml.MarkupBuilder;

import org.hamcrest.Matcher;
import org.xml.sax.SAXException;

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
  
  public static Object jsonParser(String json){
    return (new JsonSlurper()).parseText(json);
  }
  
  public static MarkupBuilder xmlBuilder(Closure c){
    MarkupBuilder builder = new MarkupBuilder();
    return builder;
  }
  
  public static Object xmlParser(String xml){
    try {
      return (new XmlSlurper()).parseText(xml);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
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
