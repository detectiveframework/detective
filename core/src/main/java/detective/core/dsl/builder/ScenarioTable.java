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
    if (rows.size() < 2)
      throw new DslException("datatable required at least 2 rows, first row for column names, the rest for the data.");
    
    List<Row> table = makeSureExistsDataTable();
    
    List<Row> existsRows = new ArrayList<Row>();
    if (table != null && table.size() > 1){
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
    
    //this.setProperty(DslBuilder.DATATABLE_PARAMNAME, ImmutableList.copyOf(existsRows));
    return existsRows;
  }

  private List<Row> makeSureExistsDataTable() {
    List<Row> table = null;
    //Object datatable = this.getProperties().get(DslBuilder.DATATABLE_PARAMNAME);
    Object datatable = scenario.getScenarioTable();
    if ((datatable != null)){
      if ((datatable instanceof List)){
        table = (List<Row>)datatable;
        if (table.size() == 0)
          return table;
        
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
