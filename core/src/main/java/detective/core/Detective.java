package detective.core;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import groovy.lang.Closure;
import groovy.util.XmlSlurper;
import groovy.xml.MarkupBuilder;

import org.hamcrest.Matcher;

import com.typesafe.config.Config;

import detective.common.trace.TraceRecord;
import detective.common.trace.TraceRecordBuilder;
import detective.common.trace.TraceRecorder;
import detective.common.trace.impl.TraceRecorderElasticSearchImpl;
import detective.core.dsl.builder.DslBuilder;
import detective.core.matcher.IsEqual;
import detective.core.runner.DslBuilderAndRun;
import detective.core.services.DetectiveFactory;
import detective.task.EchoTask;
import detective.task.HttpClientTask;
import detective.utils.StringUtils;

public class Detective {
  
  private enum Recorder {
    INSTANCE;
    
    private final TraceRecorderElasticSearchImpl recorder = new TraceRecorderElasticSearchImpl();

    public TraceRecorder getRecorder() {
      return recorder;
    }
  }
  
  public enum LogLevel{
    FATAL, ERROR, WARN, INFO, DEBUG, TRACE;
    
  }

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
  
  public static TraceRecord record(TraceRecord record){
    Recorder.INSTANCE.getRecorder().record(record);
    
    return record;
  }
  
  public static TraceRecord recordLog(LogLevel level, String message){
    TraceRecord record = TraceRecordBuilder.newRecord().withSimpleDateAsHashKey().getRecord();
    record.setType("log");
    record.setCaption(message);
    return record(record);
  }
  
  public static TraceRecord info(String message){
    return recordLog(LogLevel.INFO, message);
  }
  
  public static TraceRecord error(String message){
    return recordLog(LogLevel.ERROR, message);
  }
  
  public static TraceRecord error(String message, Throwable e){
    TraceRecord record = TraceRecordBuilder.newRecord()
        .withSimpleDateAsHashKey()
        .withException(e)
        .getRecord();
    record.setType("log");
    record.setCaption(message);
    
    return record(record);
  }
  
  public static TraceRecord debug(String message){
    return recordLog(LogLevel.DEBUG, message);
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
  
  public static Config getConfig(){
    return DetectiveFactory.INSTANCE.getConfig();
  }
  
}
