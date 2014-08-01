package detective.core.runner;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;

import org.codehaus.groovy.reflection.MixinInMetaClass;
import org.codehaus.groovy.runtime.GroovyCategorySupport;
import org.hamcrest.Matcher;
import org.junit.Assert;

import detective.core.dsl.DslException;
import detective.core.matcher.IsEqual;

/**
 * see groovy.time.TimeCategory
 * 
 * http://groovy.codehaus.org/Operator+Overloading
 * 
 * @author James Luo
 *
 */
public class ExpectObjectWrapperWrapper extends GroovyObjectSupport{
  
  private final Object realValue;
  
  public ExpectObjectWrapperWrapper(Object realValue){
    this.realValue = realValue;
  }
  
  @Override
  public int hashCode() {
    return realValue.hashCode();
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    
    if (obj instanceof ExpectObjectWrapperWrapper){
      ExpectObjectWrapperWrapper other = (ExpectObjectWrapperWrapper) obj;
      if (realValue == null) {
        if (other.realValue != null)
          return false;
      } else if (!realValue.equals(other.realValue))
        return false;
    }else
      if (obj instanceof PropertyToStringDelegate)
        throw new WrongPropertyNameInDslException(((PropertyToStringDelegate)obj).getFullPropertyName());
      else
        return obj.equals(this.realValue);
    
    return true;
  }

  @Override
  public String toString() {
    return "" + realValue;
  }

  public static void leftShift(Object self, Object obj){
    throw new RuntimeException("Not supported!");
    
//    if (self instanceof PropertyToStringDelegate){
//      throw new WrongPropertyNameInDslException(((PropertyToStringDelegate)self).getFullPropertyName());    
//    }
//    
//    if (obj != null){
//      if (obj instanceof Matcher){
//        Assert.assertThat("", self, (Matcher)obj);
//      }else{
//        
//      }
//    } else{
//      throw new DslException("We support org.hamcrest.Matcher only in expect section only for now. Maybe you didn't import static Matchers.*?, please note you can put a break point into your IDE for your DSL and inspect what the value is.");
//    }
  }
  
  public void leftShift(Object obj){
    if (obj != null){
      if (obj instanceof Matcher){
        assertThat(obj);
      }else{
        assertThat(IsEqual.equalTo(obj));
      }
    } else{
      throw new DslException("We support org.hamcrest.Matcher only in expect section only for now. Maybe you didn't import static Matchers.*?, please note you can put a break point into your IDE for your DSL and inspect what the value is.");
    }
  }

  private void assertThat(Object obj) {
    try {
      Assert.assertThat("", realValue, (Matcher)obj);
    } catch (AssertionError e) {
      Assert.assertThat("", this, (Matcher)obj);
    }
  }
  
  public Object minus(Object obj){
    if (obj instanceof ExpectObjectWrapperWrapper){
      Object otherValue = ((ExpectObjectWrapperWrapper)obj).realValue;
      if (realValue instanceof Integer || otherValue instanceof Integer)
        return ((Number)realValue).intValue() - ((Number)otherValue).intValue();
      else if (realValue instanceof Long || otherValue instanceof Long)
        return ((Number)realValue).longValue() - ((Number)otherValue).longValue();
      else if (realValue instanceof Number && otherValue instanceof Number)
        return ((Number)realValue).doubleValue() - ((Number)otherValue).doubleValue();
    }
      
    throw new RuntimeException("minus support only number so far, please log a issue if you got this error, Thanks");
  }

  public Object getRealValue() {
    return realValue;
  }

}
