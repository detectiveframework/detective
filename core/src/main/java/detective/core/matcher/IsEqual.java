package detective.core.matcher;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

/**
 * A equal modified for number which will ommit the data types, for example in this 
 * class, 4 equals 4L.
 * 
 * <TODO>For now the implement just compare toString(), need improve
 * 
 * @author James Luo
 *
 * @param <T>
 */
public class IsEqual<T> extends org.hamcrest.core.IsEqual<T> {

  private final Object expectedValue;
  
  public IsEqual(T equalArg) {
    super(equalArg);
    this.expectedValue = equalArg;
  }
  
  @Override
  public boolean matches(Object actualValue) {
    boolean matched = super.matches(actualValue);
    
    if (!matched && actualValue != null){
      //we do more for numbers as sometime user don't care the data type, for example 4 != 4L
      if (actualValue instanceof Number){
        return actualValue.toString().equals(expectedValue.toString());
      }
    }
    
    return matched;
  }
  
  @Factory
  public static <T> Matcher<T> equalTo(T operand) {
      return new IsEqual<T>(operand);
  }

}
