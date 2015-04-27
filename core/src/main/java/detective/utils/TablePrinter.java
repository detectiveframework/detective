package detective.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import detective.common.utils.ConsoleTable;
import detective.core.dsl.WrappedObject;
import detective.core.dsl.table.Row;


public class TablePrinter {
  
  public static String printObjectAsTable(Object root,String title){
    root = Utils.getRealValue(root);
    
    if (root == null)
      return printEmpty(title);
    
    if (root instanceof List){
      List list = (List)root;
      if (list.size() == 0)
        return printEmpty(title);
      
      Object firstObject = list.get(0);
      if (firstObject instanceof Map){
        return printJSON(list, title);
      }else if (firstObject instanceof Row){
        return printTable(list, title);
      }else
        throw new RuntimeException("Print object item have to be a Map or a table Row your type:" + firstObject.getClass().getName());
    }else{
      throw new RuntimeException("Print object have to be a List, your type:" + root.getClass().getName());
    }
  }
  
  private static String printEmpty(String title){
    return "\n|===========" + title + "================|\n" + "| There is no data to display |\n";
  }

  /**
   * 
   * @param root A groovy JSON Object, actually it's a List<Map>
   * @param title
   * @return
   */
  static String printJSON(Object root,String title){
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
            table.appendColum("\"" + key + "\"");
        }
        
        table.appendRow();
        for (Object value : map.values()){
          if (value instanceof String)
            value = "\"" + value + "\"";
          
          table.appendColum(value);
        }
        idx = idx + 1;
      }
    }
    
    return "\n" + table.toString();
  }
  
 
  /**
   * Print a table
   * @param table
   * @param title
   * @return
   */
  static String printTable(Object table, String title){
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
          t.appendColum("\"" + header + "\"");
      }
      t.appendRow();
      for (String header : headers){
        Object value = row.getProperty(header);
        if (value instanceof String)
          value = "\"" + value + "\"";
        t.appendColum(value);
      }
      
      idx = idx + 1;
    }
      
    return "\n" + t.toString();
  }

}
