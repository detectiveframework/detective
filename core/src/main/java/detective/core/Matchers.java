package detective.core;

import org.hamcrest.Matcher;

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
  
}
