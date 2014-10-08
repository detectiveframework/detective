package detective.core;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;

import detective.core.matcher.IsEqual;

public class Matchers {

  public static <T> org.hamcrest.Matcher<T> is(T param1) {
    return org.hamcrest.core.Is.is(param1);
  }

  public static <T> org.hamcrest.Matcher<T> is(java.lang.Class<T> param1) {
    return org.hamcrest.core.Is.is(param1);
  }

  public static <T> org.hamcrest.Matcher<T> is(org.hamcrest.Matcher<T> param1) {
    return org.hamcrest.core.Is.is(param1);
  }
  
  public static <T> Matcher<T> equalTo(T operand) {
    return IsEqual.equalTo(operand);
  }
  
  public static <T> Matcher<T> not(T operand) {
    return IsNot.not(operand);
  }
  
  /**
   * Creates a matcher that matches if examined object is <code>null</code>.
   * <p/>
   * For example:
   * <pre>assertThat(cheese, is(nullValue())</pre>
   * 
   */
  public static Matcher<Object> nullValue() {
      return new IsNull<Object>();
  }

  /**
   * A shortcut to the frequently used <code>not(nullValue())</code>.
   * <p/>
   * For example:
   * <pre>assertThat(cheese, is(notNullValue()))</pre>
   * instead of:
   * <pre>assertThat(cheese, is(not(nullValue())))</pre>
   * 
   */
  public static Matcher<Object> notNullValue() {
      return IsNot.not(nullValue());
  }
  
}
