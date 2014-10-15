package detective.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;

public class GroovyUtils {
  
  private static final MetaClassRegistry registry = MetaClassRegistryImpl.getInstance(MetaClassRegistryImpl.LOAD_DEFAULT);;
  
  public static Object getProperty(Object object, String property) {
    //still have get property? it's maybe a map or list
    if (object != null){
      if (object instanceof GroovyObjectSupport){
        Object value = ((GroovyObjectSupport)object).getProperty(property);
        return value;
      }else{     
        MetaClass metaClass = registry.getMetaClass(object.getClass());
        if (metaClass != null){
          Object value = metaClass.getProperty(object, property);
          return value;
        }
      } 
    }
   
    //TODO support java bean 
    //return BeanUtils.getProperty(realValue, property);
    return null;
  }
  
}
