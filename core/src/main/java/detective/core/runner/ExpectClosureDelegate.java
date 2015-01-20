package detective.core.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matcher;
import org.junit.Assert;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import detective.core.Parameters;
import detective.core.Scenario;
import detective.core.Story;
import detective.core.TestTask;
import detective.core.dsl.DslException;
import detective.core.dsl.ParametersImpl;
import detective.core.dsl.builder.DslBuilder;
import detective.core.dsl.table.Row;
import detective.core.dsl.table.TableParser;
import geb.Browser;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.GroovyRuntimeException;
import groovy.util.Expando;

/**
 * Delegate for expect closure.
 * <pre>
      then "I should have four black sweaters in stock"{
        sweater.balck << equalTo(4)
      }
 * </pre>
 * <br>
 * The operation << leftShift will actually runs the code, but which code 
 * will run will delegate out.
 * <br>
 * @author James Luo
 *
 */
public class ExpectClosureDelegate extends PropertyToStringDelegate{
  
  private Browser browser;
  
  /**
   * Create a new ROOT expect closure delegate
   * 
   * @param values
   */
  public ExpectClosureDelegate(Parameters values){
    super(values);
  }
  
  /**
   * 
   * @param parent can not be null
   * @param values
   */
  public ExpectClosureDelegate(PropertyToStringDelegate parent, String propertyName, Parameters values){
    super(parent, propertyName, values);
  }
  
  
  public void leftShift(Object obj){
    //Invoked here? user must typed the property wrong, we throw exception and tell user what's the problem property
    throw new WrongPropertyNameInDslException(this.getFullPropertyName());    
  }
  
  public Object getProperty(String property){
//    if (browser != null){
//      browser.getPage();
//      Object gebField = ((GroovyObject)browser).getProperty(property);
//      if (gebField != null)
//        return gebField;
//    }
//      
    Object result = super.getProperty(property);
    if (! (result instanceof PropertyToStringDelegate)){
      return new ExpectObjectWrapperWrapper(result);
    }else
      return result;
  }
  
  protected PropertyToStringDelegate newNextLevelProperty(PropertyToStringDelegate parent, String propertyName){
    return new ExpectClosureDelegate(parent, propertyName, values);
  }
  
  public void runtask(Object obj){
    throw new DslException("implement interface TestTask and your class is " + obj.getClass().getName());
  }
  
  public void runtask(TestTask task){
    Parameters datain = this.getValues();
    
    Parameters dataReturned = task.execute(datain);
    
    this.getValues().putAllUnwrappered(dataReturned);
  }
  
  public void runtask(Map<?, ?> parameters, TestTask task){
    for (Object key : parameters.keySet()){
      if (key != null)
        this.getValues().put(key.toString(), parameters.get(key));
    }
    this.runtask(task);
  }
  
  public void runtask(List<?> tasks){
    for (Object item : tasks){
      if (item instanceof TestTask){
        runtask((TestTask)item);
      }
    }
  }
  
  public List<Row> table(Closure<?> closure){
    return TableParser.asListOfRows(values, closure);
  }
 
  public void expect(String errorMsg, Closure<?> closure){
    List<String> list = new ArrayList<String>();
    list.add(errorMsg);
    expect(list, closure);
  }

  private void errorMsgContains(String errorMsg, Throwable e) throws AssertionError {
    if (e.getMessage() == null && errorMsg == null)
      return;
    
    if (e.getMessage() == null && errorMsg != null)
      throw new AssertionError("Get a null error message but expect " + errorMsg);
    
    if (e.getMessage() != null){
      if (!e.getMessage().contains(errorMsg)){
        throw new AssertionError("\nExpected: " + errorMsg + "\n     got:" + e.getMessage());
      }
    }
  }
  
  public void expect(List<String> errorMsgs, Closure<?> closure){
    try {
      closure.setDelegate(this);
      closure.setResolveStrategy(Closure.DELEGATE_FIRST);
      closure.call();
    } catch (Throwable e) {
      for (String errorMsg : errorMsgs)
        errorMsgContains(errorMsg, e);
    }
  }
  
  public Object invokeMethod(String name, Object args) {
    try {
      return super.invokeMethod(name, args);
    }
    catch (GroovyRuntimeException e) {    
      if (browser != null){
        return ((GroovyObject)browser).invokeMethod(name, args);
      }else{
        throw e;
      }
    }
  }

  public Browser getBrowser() {
    return browser;
  }

  public void setBrowser(Browser browser) {
    this.browser = browser;
  }
}