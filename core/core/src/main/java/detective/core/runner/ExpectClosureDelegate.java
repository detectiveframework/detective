package detective.core.runner;

import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.Assert;

import com.google.common.base.Optional;

import detective.core.dsl.DslException;
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
  
  /**
   * Create a new ROOT expect closure delegate
   * 
   * @param values
   */
  public ExpectClosureDelegate(Map<String, Object> values){
    super(values);
  }
  
  /**
   * 
   * @param parent can not be null
   * @param values
   */
  public ExpectClosureDelegate(PropertyToStringDelegate parent, String propertyName, Map<String, Object> values){
    super(parent, propertyName, values);
  }
  
  
  public void leftShift(Object obj){
    //Invoked here? user must typed the property wrong, we throw exception and tell user what's the problem property
    throw new WrongPropertyNameInDslException(this.getFullPropertyName());    
  }
  
  public Object getProperty(String property){
    Object result = super.getProperty(property);
    if (! (result instanceof PropertyToStringDelegate)){
      return new ExpectObjectWrapperWrapper(result);
    }else
      return result;
  }
  
  protected PropertyToStringDelegate newNextLevelProperty(PropertyToStringDelegate parent, String propertyName){
    return new ExpectClosureDelegate(parent, propertyName, values);
  }
  
}