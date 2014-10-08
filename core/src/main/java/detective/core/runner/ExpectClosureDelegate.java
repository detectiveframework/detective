package detective.core.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matcher;
import org.junit.Assert;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import detective.core.Parameters;
import detective.core.Scenario;
import detective.core.Story;
import detective.core.TestTask;
import detective.core.dsl.DslException;
import detective.core.dsl.ParametersImpl;
import detective.core.dsl.builder.DslBuilder;
import detective.core.dsl.table.Row;
import detective.core.dsl.table.TableParser;
import groovy.lang.Closure;
import groovy.util.Expando;

/**
 * Delegate for expect closure.
 * <pre>
      then "I should have four black sweaters in stock"{
        sweater.balck << equalTo(4)
      }
 * </pre>
 * <br>
 * The operation << leftShift will actually runs the code, but which code 
 * will run will delegate out.
 * <br>
 * @author James Luo
 *
 */
public class ExpectClosureDelegate extends PropertyToStringDelegate{
  
  /**
   * Create a new ROOT expect closure delegate
   * 
   * @param values
   */
  public ExpectClosureDelegate(Parameters values){
    super(values);
  }
  
  /**
   * 
   * @param parent can not be null
   * @param values
   */
  public ExpectClosureDelegate(PropertyToStringDelegate parent, String propertyName, Parameters values){
    super(parent, propertyName, values);
  }
  
  
  public void leftShift(Object obj){
    //Invoked here? user must typed the property wrong, we throw exception and tell user what's the problem property
    throw new WrongPropertyNameInDslException(this.getFullPropertyName());    
  }
  
  public Object getProperty(String property){
    Object result = super.getProperty(property);
    if (! (result instanceof PropertyToStringDelegate)){
      return new ExpectObjectWrapperWrapper(result);
    }else
      return result;
  }
  
  protected PropertyToStringDelegate newNextLevelProperty(PropertyToStringDelegate parent, String propertyName){
    return new ExpectClosureDelegate(parent, propertyName, values);
  }
  
  public void runtask(Object obj){
    throw new DslException("implement interface TestTask and your class is " + obj.getClass().getName());
  }
  
  public void runtask(TestTask task){
    Parameters datain = this.getValues();
    
    Parameters dataReturned = task.execute(datain);
    
    this.getValues().putAllUnwrappered(dataReturned);
  }
  
  public List<Row> table(Closure<?> c){
    List<Row> rows = TableParser.asListOfRows(values, c);
    if (rows.size() < 2)
      throw new DslException("datatable required at least 2 rows, first row for column names, the rest for the data.");
    
    return rows;
  }
 
}