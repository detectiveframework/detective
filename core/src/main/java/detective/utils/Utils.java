package detective.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import detective.core.dsl.WrappedObject;

public class Utils {

  /**
   * Get Real Value
   * 
   * @param obj
   * @return
   */
  public static Object getRealValue(Object obj){
    if (obj == null)
      return null;
    
    if (obj instanceof WrappedObject)
      return ((WrappedObject<?>)obj).getValue();
    
    if (obj instanceof Object[]){
      Object[] array = (Object[])obj;
      Object[] results = new Object[array.length];
      for (int i = 0; i < array.length; i++){
        Object item = array[i];
        results[i] = getRealValue(item);
      }
      return results;
    }
    
    return obj;
  }
  
  public static String getStackTrace(Throwable aThrowable) {
    Writer result = new StringWriter();
    PrintWriter printWriter = new PrintWriter(result);
    aThrowable.printStackTrace(printWriter);
    return result.toString();
  }
  
}
