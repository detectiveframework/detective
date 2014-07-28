package detective.core.dsl.table;

import java.util.ArrayList;
import java.util.List;

public class Row {
  
  private List<Object> values = new ArrayList<Object>();
  
  public Row(Object firstValue){
    this.values.add(firstValue);
  }

  public Row or(Object arg) {
    values.add(arg);
    return this;
  }

  public Object[] asArray() {
    return values.toArray();
  }

  @Override
  public String toString() {
    return "Row " + values;
  }

}
