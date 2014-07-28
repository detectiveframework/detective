package detective.core.runner;

import org.hamcrest.Matcher;
import org.junit.Assert;

import detective.core.dsl.DslException;

/**
 * see groovy.time.TimeCategory
 * 
 * @author James Luo
 *
 */
public class ExpectObjectWrapper {
  
//  private final Object obj;
//  public ExpectObjectWrapper(Object obj){
//    this.obj = obj;
//  }
//  
//  public static Object mixin(Closure<?> closure){
//    return GroovyCategorySupport.use(ExpectObjectWrapper.class, closure); 
//  }
  
  public static void leftShift(Object self, Object obj){
    if (self instanceof PropertyToStringDelegate){
      throw new WrongPropertyNameInDslException(((PropertyToStringDelegate)self).getFullPropertyName());    
    }
    
    if (obj != null && obj instanceof Matcher){
      Assert.assertThat("", self, (Matcher)obj);
    }else{
      throw new DslException("We support org.hamcrest.Matcher only in expect section only for now. Maybe you didn't import static Matchers.*?, please note you can put a break point into your IDE for your DSL and inspect what the value is.");
    }
  }

}
