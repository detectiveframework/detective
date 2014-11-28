package detective.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import detective.common.utils.ConsoleTable;
import detective.core.dsl.WrappedObject;
import detective.core.dsl.table.Row;


class TablePrinter {

  /**
   * 
   * @param root A groovy JSON Object, actually it's a List<Map>
   * @param title
   * @return
   */
  public static String printJSON(Object root,String title){
    root = Utils.getRealValue(root);
   
    ConsoleTable table =null;
    
    if (root instanceof List){
      int idx = 0;
      for (Object subItem : (List)root){
        if (! (subItem instanceof Map))
          throw new RuntimeException("List item have to be a Map type");
        
        Map<?, ?> map = (Map)subItem;
        if(idx==0){
          Set<?> keySet = map.keySet();
          table = new ConsoleTable(keySet.size(), title);
          table.appendRow();
          for (Object key : keySet)
            table.appendColum(key);
        }
        
        table.appendRow();
        for (Object value : map.values()){
          table.appendColum(value);
        }
        idx = idx + 1;
      }
    }
    
    return table.toString();
  }
  
 
  /**
   * Print a table
   * @param table
   * @param title
   * @return
   */
  public static String printTable(Object table, String title){
    ConsoleTable t = null;
    table = Utils.getRealValue(table);
    
    List<Row> rows = (List<Row>)table;
    int idx = 0;
    for (Row row : rows){
      String[] headers = row.getHeaderAsStrings();
      if (idx == 0){
        t = new ConsoleTable(headers.length, title);
        t.appendRow();
        for (String header : headers)
          t.appendColum(header);
      }
      t.appendRow();
      for (String header : headers){
        t.appendColum(row.getProperty(header));
      }
      
      idx = idx + 1;
    }
      
    return t.toString();
  }

}
