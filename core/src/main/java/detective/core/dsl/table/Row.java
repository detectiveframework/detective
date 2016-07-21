package detective.core.dsl.table;

import groovy.lang.GroovyObject;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MetaClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scala.reflect.internal.Trees.This;
import detective.core.dsl.DslException;
import detective.core.runner.PropertyToStringDelegate;

public class Row extends GroovyObjectSupport implements GroovyObject{
  
  private List<Object> values = new ArrayList<Object>();
  private Map<Object, Integer> headToIndexMap = new HashMap<Object, Integer>();
  private Row header = null;
  
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
  
  public Map<String, Object> asMap(){
    Map<String, Object> map = new HashMap<String, Object>();
    for (String head : getHeaderAsStrings()){
      map.put(head, this.getProperty(head));
    }
    return map;
  }
  
  public int size(){
    return values.size();
  }

  @Override
  public String toString() {
    return "Row " + values + " Header " + header.values;
  }
  
  /**
   * Setup header if have
   */
  public void setRowHeader(Row header){
    this.header = header;
    
    if (header == null || header.size() == 0)
      return;
    
    if (header.size() != this.size())
      throw new DslException("Size of header and row not same, header we got " + header + " row we got: " + this);
    
    this.headToIndexMap.clear();
    for (int i = 0; i < header.size(); i++){
      headToIndexMap.put(getHeaderText(header.values.get(i)), i);
    }
  }
  
  public boolean headExists(String header){
    return headToIndexMap.containsKey(header);
  }
  
  public Row getHeader(){
    return this.header;
  }
  
  public String[] getHeaderAsStrings(){
    Row headerRow = getHeader();
    List<String> headers = new ArrayList<String>();
    for (Object obj : headerRow.asArray()){
      String header = Row.getHeaderText(obj);
      headers.add(header);
    }
    return headers.toArray(new String[]{});
  }

  @Override
  public Object getProperty(String propertyName) {
    if (this.headToIndexMap.containsKey(propertyName))
      return values.get(headToIndexMap.get(propertyName));
    
    return super.getProperty(propertyName);
  }
  
  public static String getHeaderText(Object obj){
    String header = obj.toString();
    if (obj instanceof PropertyToStringDelegate)
      header = ((PropertyToStringDelegate)obj).getFullPropertyName();
    return header;
  }

}
