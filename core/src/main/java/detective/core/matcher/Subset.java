package detective.core.matcher;

import groovy.lang.Closure;

import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.Assert;

import detective.core.dsl.DslException;
import detective.core.dsl.table.Row;
import detective.utils.GroovyUtils;

/**
 * Test if a type is a subset of other
 * 
 * @author James Luo
 *
 * @param <T>
 */
public class Subset<T> extends BaseMatcher<T> {

  private final Object expectedValue;
  
  public Subset(T expectedValue) {
    this.expectedValue = expectedValue;
  }
  
  @Factory
  public static <T> Matcher<T> subsetOf(T operand) {
      return new Subset<T>(operand);
  }
  
  @Override
  public boolean matches(Object actualValue) {
    isMatches(actualValue, expectedValue);
    
    return true;
  }
  
  @Factory
  public static <T> Matcher<T> subset(T operand) {
      return new Subset<T>(operand);
  }

  @Override
  public void describeTo(Description description) {
    description.appendValue(expectedValue);
  }
  
  private void isMatches(Object actualValue, Object expectedValue){
    //List
    if (actualValue instanceof List && expectedValue instanceof List){
      final List<?> actualList = (List<?>)actualValue;
      final List<?> expectedList = (List<?>)expectedValue;
      
      if (actualList.size() == 0)
        throw new java.lang.AssertionError("List can't be empty:" + actualList);
      
      if (expectedList.size() == 0)
        throw new java.lang.AssertionError("List can't be empty:" + expectedList);
      
      if (actualList.size() < expectedList.size())
        throw new java.lang.AssertionError("acutal data is smaller then expected list. actual item size:" + actualList.size() + " expected item size:" + expectedList.size());
      
      //The first column is the key, which identify how to identify a row
      //it can be a row number (if it's a number), or it's a closure which return a boolean to identify a row
      Object expectedItem = expectedList.get(0);
      
      
      if (expectedItem instanceof Row){
        //we support row/table first
        Row row = (Row)expectedItem;
        Object actualRowFound = findRightRowInActualList(actualList, row);
        if (actualRowFound == null){
          throw new java.lang.AssertionError("couldn't found any item in actual list based on first column of row" + row);
        }
        compareTwoRow(actualRowFound, row);
      }
    }else{
      throw new java.lang.AssertionError("subset for now support only list to list. you types " + actualValue.getClass() + ":" + expectedValue.getClass());
    }
  }
  
  private void compareTwoRow(Object actualRow, Row expectedRow) throws AssertionError{
    String[] headers = expectedRow.getHeaderAsStrings();
    for (int i = 1; i < headers.length; i++){
      String propertyName = headers[i];
      Object actualProperty = this.getPropertyInner(actualRow, propertyName);
      Assert.assertThat(actualProperty, IsEqual.equalTo(expectedRow.getProperty(propertyName)));
    }
  }

  private Object findRightRowInActualList(final List<?> actualList, Row row)
      throws AssertionError {
    String[] headers = row.getHeaderAsStrings();
    
    final Object firstColumn = getPropertyInner(row, headers[0]);
    Closure<Object> rowSearcher = null;
    if (firstColumn instanceof Number){
      //Number
      rowSearcher = new Closure<Object>(this, actualList){
        public Object call() {
          return actualList.get(((Number)firstColumn).intValue());
        }
      };
    }else if (firstColumn instanceof Closure){
      //A closure which return boolean
      final Closure booleanSearch = (Closure)firstColumn;
      rowSearcher = new Closure<Object>(this, actualList){
        public Object call() {
          for (Object item : actualList){
            Object resultFromBooleanSearch = booleanSearch.call(item); 
            if (resultFromBooleanSearch instanceof Boolean){
              if (Boolean.TRUE.equals(booleanSearch))
                return item;
            }
          }
          return null;
        }
      }; 
    }else{
      throw new java.lang.AssertionError("the first column of your table used for located a row when compare between two tables, this column should either a number which identify the row index, or a search expression, please see document for more information.");
    }
    
    Object actualRowFound = rowSearcher.call();
    return actualRowFound;
  }
  
  private Object getPropertyInner(Object object, String propertyName){
    return GroovyUtils.getProperty(object, propertyName);
  }
}
