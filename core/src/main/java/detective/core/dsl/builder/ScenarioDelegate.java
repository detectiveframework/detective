package detective.core.dsl.builder;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import detective.core.Parameters;
import detective.core.TestTask;
import detective.core.dsl.DslException;
import detective.core.dsl.SimpleScenario;
import detective.core.dsl.table.Row;
import detective.core.dsl.table.TableParser;
import detective.core.runner.PropertyToStringDelegate;

public class ScenarioDelegate extends ShareDataAwardDelegate{    
  protected String title;
  protected SimpleScenario scenario;
  protected Closure<?> closure;
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public SimpleScenario getScenario() {
    return scenario;
  }

  public void setScenario(SimpleScenario scenario) {
    this.scenario = scenario;
  }

  public Closure<?> getClosure() {
    return closure;
  }

  public void setClosure(Closure<?> closure) {
    this.closure = closure;
  }

  public ScenarioDelegate(Parameters values){
    super(values);
  }
  
  public ScenarioDelegate(PropertyToStringDelegate parent, String propertyName, Parameters values){
    super(parent, propertyName, values);
  }
  
  protected PropertyToStringDelegate newNextLevelProperty(PropertyToStringDelegate parent, String propertyName){
    return new ScenarioDelegate(parent, propertyName, values);
  }
  
  
  protected Object getPropertyInnernal(String property){
    Object value = values.getUnwrappered(getFullPropertyName(property));
    return value;
  }
  
  public TestTask runtask(TestTask task){
    this.scenario.addTask(task);
    return task;
  }
  
  public List<Row> datatable(Closure<?> c){
    List<Row> rows = TableParser.asListOfRows(values, c);
    if (rows.size() < 2)
      throw new DslException("datatable required at least 2 rows, first row for column names, the rest for the data.");
    
    List<Row> table = makeSureExistsDataTable();
    
    List<Row> existsRows = new ArrayList<Row>();
    if (table != null){
      if (! checkHasSameColumn(table.get(0), rows.get(0))){
        throw new DslException("So far we support only one type of datatable in all given section. datatable must have same header and columns, exists datatable:" + table.get(0) + " your datatable " + rows.get(0));
      };
      
      for (Row row : table){
        existsRows.add(row);
      }
    }
    
    if (existsRows.size() > 1)
      rows.remove(0); //remove header
    
    for (Row row : rows)
      existsRows.add(row);
    
    this.setProperty(DslBuilder.DATATABLE_PARAMNAME, ImmutableList.copyOf(existsRows));
    return rows;
  }

  private List<Row> makeSureExistsDataTable() {
    List<Row> table = null;
    Object datatable = this.getProperties().get(DslBuilder.DATATABLE_PARAMNAME);
    if ((datatable != null)){
      if ((datatable instanceof List)){
        table = (List<Row>)datatable;
        if (table.size() < 2 && (! (table.get(0) instanceof Row))){
          throw new DslException("You have a parameter named " + DslBuilder.DATATABLE_PARAMNAME + " exists but not a DataTable type, please check document for Datatable.");
        }
      }else{
        throw new DslException("You have a parameter named " + DslBuilder.DATATABLE_PARAMNAME + " exists but not a DataTable type, please check document for Datatable.");
      }
    }
    return table;
  }
  
  private boolean checkHasSameColumn(Row oldRow, Row newRole){
    Object[] oldArray = oldRow.asArray();
    Object[] newArray = newRole.asArray();
    
    if (oldArray.length != newArray.length)
      return false;
    
    for (int i = 0; i < oldArray.length; i++){
      Object oldValue = oldArray[i];
      if (! oldValue.equals(newArray[i]))
        return false;
    }
    
    return true;
  }
}