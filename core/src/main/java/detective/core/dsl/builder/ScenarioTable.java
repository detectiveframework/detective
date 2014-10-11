package detective.core.dsl.builder;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import detective.core.Parameters;
import detective.core.Scenario;
import detective.core.dsl.DslException;
import detective.core.dsl.ParametersImpl;
import detective.core.dsl.table.Row;
import detective.core.dsl.table.TableParser;

public class ScenarioTable {

  private final Scenario scenario;
  
  //The parameters pass into table
  private Parameters values = new ParametersImpl();
  
  public ScenarioTable(Scenario scenario){
    this.scenario = scenario;
  }
  
  public List<Row> scenarioTable(Closure<?> c){
    List<Row> rows = TableParser.asListOfRows(values, c);
    
    List<Row> table = makeSureExistsDataTable();
    
    List<Row> existsRows = new ArrayList<Row>();
    if (table != null && table.size() > 0){
      if (! checkHasSameColumn(table.get(0), rows.get(0))){
        throw new DslException("Scenario table must have same header and columns, exists table:" + table.get(0) + " your datatable " + rows.get(0));
      };
      
      for (Row row : table){
        existsRows.add(row);
      }
    }
    
    for (Row row : rows)
      existsRows.add(row);
    
    //this.setProperty(DslBuilder.DATATABLE_PARAMNAME, ImmutableList.copyOf(existsRows));
    return existsRows;
  }

  private List<Row> makeSureExistsDataTable() {
    return scenario.getScenarioTable();
  }
  
  private boolean checkHasSameColumn(Row oldRow, Row newRole){
    Object[] oldArray = oldRow.getHeader().asArray();
    Object[] newArray = newRole.getHeader().asArray();
    
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
