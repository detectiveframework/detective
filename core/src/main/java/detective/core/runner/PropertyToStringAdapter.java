package detective.core.runner;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MetaExpandoProperty;
import groovy.lang.MissingPropertyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PropertyToStringAdapter extends GroovyObjectSupport {

  private Map<String, Object> expandoProperties;

  public PropertyToStringAdapter() {
  }

  /**
   * @return the dynamically expanded properties
   */
  public Map<String, Object> getProperties() {
    if (expandoProperties == null) {
      expandoProperties = createMap();
    }
    return expandoProperties;
  }

  public List getMetaPropertyValues() {
    // run through all our current properties and create MetaProperty objects
    List ret = new ArrayList();
    for (Object o : getProperties().entrySet()) {
      Entry entry = (Entry) o;
      ret.add(new MetaExpandoProperty(entry));
    }

    return ret;
  }

  public Object getProperty(String property) {
    // always use the expando properties first
    Object result = getProperties().get(property);
    if (result != null)
      return result;
    try {
      return super.getProperty(property);
    } catch (MissingPropertyException e) {
      // IGNORE
    }
    return null;
  }

  public void setProperty(String property, Object newValue) {
    // always use the expando properties
    getProperties().put(property, newValue);
  }

  public Object invokeMethod(String name, Object args) {
    return super.invokeMethod(name, args);
  }

  /**
   * This allows toString to be overridden by a closure <i>field</i> method attached to the expando
   * object.
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {
    Object method = getProperties().get("toString");
    if (method != null && method instanceof Closure) {
      // invoke overridden toString closure method
      Closure closure = (Closure) method;
      closure.setDelegate(this);
      return closure.call().toString();
    } else {
      return expandoProperties.toString();
    }
  }

  /**
   * This allows equals to be overridden by a closure <i>field</i> method attached to the expando
   * object.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    Object method = getProperties().get("equals");
    if (method != null && method instanceof Closure) {
      // invoke overridden equals closure method
      Closure closure = (Closure) method;
      closure.setDelegate(this);
      Boolean ret = (Boolean) closure.call(obj);
      return ret.booleanValue();
    } else {
      return super.equals(obj);
    }
  }

  /**
   * This allows hashCode to be overridden by a closure <i>field</i> method attached to the expando
   * object.
   * 
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    Object method = getProperties().get("hashCode");
    if (method != null && method instanceof Closure) {
      // invoke overridden hashCode closure method
      Closure closure = (Closure) method;
      closure.setDelegate(this);
      Integer ret = (Integer) closure.call();
      return ret.intValue();
    } else {
      return super.hashCode();
    }
  }

  /**
   * Factory method to create a new Map used to store the expando properties map
   * 
   * @return a newly created Map implementation
   */
  protected Map createMap() {
    return new HashMap();
  }

}