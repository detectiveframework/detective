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

  private final Object fullValue;
  
  public Subset(T fullValue) {
    this.fullValue = fullValue;
  }
  
  @Factory
  public static <T> Matcher<T> subsetOf(T operand) {
      return new Subset<T>(operand);
  }
  
  @Override
  public boolean matches(Object subValue) {
    isMatches(fullValue, subValue);
    
    return true;
  }
  
  @Factory
  public static <T> Matcher<T> subset(T operand) {
      return new Subset<T>(operand);
  }

  @Override
  public void describeTo(Description description) {
    description.appendValue(fullValue);
  }
  
  private void isMatches(Object fullValue, Object subValue){
    //List
    if (fullValue instanceof List && subValue instanceof List){
      final List<?> fulllList = (List<?>)fullValue;
      final List<?> subList = (List<?>)subValue;
      
      if (fulllList.size() == 0)
        throw new java.lang.AssertionError("List can't be empty:" + fulllList);
      
      if (subList.size() == 0)
        throw new java.lang.AssertionError("List can't be empty:" + subList);
      
      if (fulllList.size() < subList.size())
        throw new java.lang.AssertionError("acutal data is smaller then expected list. actual item size:" + fulllList.size() + " expected item size:" + subList.size());
      
      matchToList(fulllList, subList);
    }else{
      throw new java.lang.AssertionError("subset for now support only list to list. you types " + fullValue.getClass() + ":" + subValue.getClass());
    }
  }

  private void matchToList(final List<?> fulllList, final List<?> subList) throws AssertionError {
    //The first column is the key, which identify how to identify a row
    //it can be a row number (if it's a number), or it's a closure which return a boolean to identify a row
    for (Object expectedItem : subList){
      if (expectedItem instanceof Row){
        //we support row/table first
        Row row = (Row)expectedItem;
        Object actualRowFound = findRightRowInFullList(fulllList, row);
        if (actualRowFound == null){
          throw new java.lang.AssertionError("couldn't found any item in actual list based on first column of row" + row);
        }
        compareTwoRow(actualRowFound, row);
      }else{
        throw new java.lang.AssertionError("Subset have to be a Row type, type you provided: " + expectedItem.getClass().getName());
      }
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

  private Object findRightRowInFullList(final List<?> fullList, Row row)
      throws AssertionError {
    String[] headers = row.getHeaderAsStrings();
    
    final Object firstColumn = getPropertyInner(row, headers[0]);
    Closure<Object> rowSearcher = null;
    if (firstColumn instanceof Number){
      //Number
      rowSearcher = new Closure<Object>(this, fullList){
        public Object call() {
          return fullList.get(((Number)firstColumn).intValue());
        }
      };
    }else if (firstColumn instanceof Closure){
      //A closure which return boolean
      final Closure booleanSearch = (Closure)firstColumn;
      rowSearcher = new Closure<Object>(this, fullList){
        public Object call() {
          for (Object item : fullList){
            Object resultFromBooleanSearch = booleanSearch.call(item); 
            if (resultFromBooleanSearch instanceof Boolean){
              if (Boolean.TRUE.equals(resultFromBooleanSearch))
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
