package detective.core;

import geb.Browser;
import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import groovy.lang.Closure;
import groovy.util.XmlSlurper;
import groovy.xml.MarkupBuilder;

import java.util.Arrays;

import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.IsNot;

import com.typesafe.config.Config;

import detective.common.trace.TraceRecord;
import detective.common.trace.TraceRecordBuilder;
import detective.common.trace.TraceRecorder;
import detective.common.trace.impl.TraceRecorderElasticSearchImpl;
import detective.core.dsl.WrappedObject;
import detective.core.dsl.builder.DslBuilder;
import detective.core.matcher.IsEqual;
import detective.core.matcher.Subset;
import detective.core.runner.DslBuilderAndRun;
import detective.core.services.DetectiveFactory;
import detective.task.EchoTask;
import detective.task.HttpClientTask;
import detective.utils.StringUtils;

/**
 * The Factory / Entry Point class for Detective Framework
 * 
 * @author James Luo
 *
 */
public class Detective {
  
  private enum Recorder {
    INSTANCE;
    
    private final transient TraceRecorderElasticSearchImpl recorder = new TraceRecorderElasticSearchImpl();

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
  
  public static <T> Matcher<T> subsetOf(T operand) {
    if (operand != null && operand instanceof WrappedObject){
      operand = (T)((WrappedObject)operand).getValue();
    }
    return Subset.subsetOf(operand);
  }
  
  public static <T> Matcher<T> not(T value) {
    return IsNot.not(equalTo(value));
  }
  
  /**
   * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
   * <p/>
   * For example:
   * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
   */
  public static <T> Matcher<T> allOf(Matcher<? super T>... matchers) {
      return AllOf.allOf(Arrays.asList(matchers));
  }

  /**
   * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
   * <p/>
   * For example:
   * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
   */
  public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second) {
      return AllOf.allOf(first, second);
  }

  /**
   * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
   * <p/>
   * For example:
   * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
   */
  public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third) {
      return AllOf.allOf(first, second, third);
  }

  /**
   * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
   * <p/>
   * For example:
   * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
   */
  public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth) {
      return AllOf.allOf(first, second, third, fourth);
  }

  /**
   * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
   * <p/>
   * For example:
   * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
   */
  public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth) {
      return AllOf.allOf(first, second, third, fourth, fifth);
  }

  /**
   * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
   * <p/>
   * For example:
   * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
   */
  public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth, Matcher<? super T> sixth) {
    return AllOf.allOf(first, second, third, fourth, fifth, sixth);
  }
  
  //Utilities ================
  public static String randomId() {
    return StringUtils.randomBase64UUID();
  }
  
  public static Config getConfig(){
    return DetectiveFactory.INSTANCE.getConfig();
  }
  
  
  //GEB====================
  /**
   * The browser is the centre of Geb. It encapsulates a {@link org.openqa.selenium.WebDriver} implementation and references
   * a {@link geb.Page} object that provides access to the content.
   * <p>
   * Browser objects dynamically delegate all method calls and property read/writes that it doesn't implement to the current
   * page instance via {@code propertyMissing()} and {@code methodMissing()}.
   * 
   * If a call come from This class, it resolved first
   * Then get.Browser
   * Then currentPage
   * 
   * @see geb.Browser
   * @return
   */
  public static Browser newBrowser(){
    Browser browser = new Browser();
    //Setup driver
    //browser.setDriver(null);
    browser.setBaseUrl(getConfig().getString("global.baseurl"));
    return browser;
  }
  
  /**
   * Creates a new browser object via the default constructor and executes the closure
   * with the browser instance as the closure's delegate.
   *
   * @return the created browser
   */
  public static Browser browser(Closure script) {
    Browser browser = newBrowser();
    try {
      script.setResolveStrategy(Closure.DELEGATE_FIRST);
      Browser.drive(browser, script);
    } finally{
      browser.close();
    }
    return browser;
  }
}
