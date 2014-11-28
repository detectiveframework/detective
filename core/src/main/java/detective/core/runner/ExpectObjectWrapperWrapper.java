package detective.core.runner;

import java.util.Map;

import groovy.lang.Closure;
import groovy.lang.GroovyInterceptable;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;

import org.codehaus.groovy.reflection.MixinInMetaClass;
import org.codehaus.groovy.runtime.GroovyCategorySupport;
import org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl;
import org.hamcrest.Matcher;
import org.junit.Assert;

import detective.core.dsl.DslException;
import detective.core.dsl.ParametersImpl;
import detective.core.dsl.WrappedObject;
import detective.core.matcher.IsEqual;
import detective.utils.Utils;

/**
 * see groovy.time.TimeCategory
 * 
 * http://groovy.codehaus.org/Operator+Overloading
 * 
 * @author James Luo
 *
 */
public class ExpectObjectWrapperWrapper extends GroovyObjectSupport implements WrappedObject<Object>, GroovyInterceptable{
  
  private final Object realValue;
  private final MetaClassRegistry registry;
  
  public ExpectObjectWrapperWrapper(Object realValue){
    this.realValue = realValue;
    registry = MetaClassRegistryImpl.getInstance(MetaClassRegistryImpl.LOAD_DEFAULT);
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
      try {
        Assert.assertThat("", this, (Matcher)obj);
      } catch (AssertionError e1) {
        // we got AssertionError still? most likely because of first try, we throw out the original error
        throw e;
      }
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
  
  public Object plus(Object obj){
    if (obj instanceof ExpectObjectWrapperWrapper){
      Object otherValue = ((ExpectObjectWrapperWrapper)obj).realValue;
      if (realValue instanceof Integer || otherValue instanceof Integer)
        return ((Number)realValue).intValue() + ((Number)otherValue).intValue();
      else if (realValue instanceof Long || otherValue instanceof Long)
        return ((Number)realValue).longValue() + ((Number)otherValue).longValue();
      else if (realValue instanceof Number && otherValue instanceof Number)
        return ((Number)realValue).doubleValue() + ((Number)otherValue).doubleValue();
      else if (realValue instanceof String && otherValue instanceof String)
        return ((String)realValue) + ((String)otherValue);
    }
      
    throw new RuntimeException("plus support only number and String so far, please log a issue if you got this error, Thanks. Currnet values we got:" + realValue + ":" + obj );
  }
  
  public Object getRealValue() {
    return realValue;
  }

  @Override
  public Object getValue() {
    return realValue;
  }

  @Override
  public void setValue(Object value) {
    throw new RuntimeException("We don't allow setup the value at this moment");
  }
  
  public Object getProperty(String property) {
    //still have get property? it's maybe a map or list
    if (realValue != null){
      if (realValue instanceof GroovyObjectSupport){
        Object value = ((GroovyObjectSupport)realValue).getProperty(property);
        return new ExpectObjectWrapperWrapper(value);
      }else{     
        MetaClass metaClass = registry.getMetaClass(realValue.getClass());
        if (metaClass != null){
          Object value = metaClass.getProperty(realValue, property);
          return new ExpectObjectWrapperWrapper(value);
        }
      } 
    }
    
    if (realValue == null)
      throw new DslException("you try to access a property on a null object. property name: " + property);
    
    return super.getProperty(property);
  }
  
  /**
   * All method call will goes here first as we implements GroovyInterceptable
   */
  public Object invokeMethod(String name, Object args) {
    if (name.equals("leftShift"))
      return this.getMetaClass().invokeMethod(this, name, args);
    
    if (realValue != null){
      if (realValue instanceof GroovyObjectSupport){
        Object value = ((GroovyObjectSupport)realValue).invokeMethod(name, Utils.getRealValue(args));
        return new ExpectObjectWrapperWrapper(value);
      }else {
        MetaClass metaClass = registry.getMetaClass(realValue.getClass());
        if (metaClass != null){
          Object value = metaClass.invokeMethod(realValue, name, Utils.getRealValue(args));
          return new ExpectObjectWrapperWrapper(value);
        }
      }
    }
    return getMetaClass().invokeMethod(this, name, args);
  }
  
}
