package detective.core.runner;

import java.util.Map;

import groovy.util.Expando;

import org.junit.Assert;

import detective.core.Parameters;
import detective.core.dsl.DslException;

/**
 * Convert all abc.def = "abc" into a map
 * {"abc.def":"abc"}
 *
 */
public class PropertyToStringDelegate extends Expando{
  private final String propertyName;
  private final PropertyToStringDelegate parent;
  

  protected final Parameters values;
  
  /**
   * Create a new ROOT expect closure delegate
   * 
   * @param values
   */
  public PropertyToStringDelegate(Parameters values){
    this.parent = null;
    this.propertyName = null;
    this.values = values;
  }
  
  /**
   * 
   * @param parent can not be null
   * @param values
   */
  public PropertyToStringDelegate(PropertyToStringDelegate parent, String propertyName, Parameters values){
    Assert.assertNotNull(parent);
    Assert.assertNotNull(propertyName);
    Assert.assertNotNull(values);
    
    this.parent = parent;
    this.propertyName = propertyName;
    this.values = values;
  }
  
  
  
  @Override
  public String toString() {
    return "Delegate (" + getFullPropertyName() + ")";
  }
  
  
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    
    PropertyToStringDelegate other = (PropertyToStringDelegate) obj;
    String fullName = this.getFullPropertyName();
    String otherFullName = other.getFullPropertyName();
    
    if (fullName.equals(otherFullName)){
      Object value = values.get(fullName);
      Object otherValue = other.getPropertyInnernal(null);
      if (value == null && otherValue == null)
        return true;
      else if (value != null && otherValue == null)
        return false;
      else if (value == null && otherValue != null)
        return false;
      
      return value.equals(otherValue);
    }else
      return false;
  }

  public void setProperty(String property, Object newValue) {
    Object value = super.getProperty(property);
    if (value != null && (! (newValue instanceof PropertyToStringDelegate)) && value instanceof PropertyToStringDelegate)
      throw new DslException("A ambiguousness found for parameter:\"" + this.getFullPropertyName(property) + "\", you are overwriting a parameter defined before, for example \n login.username.lastname = \"lastname\" \n login.username = \"username\" \nlogin.username.lastname will lost as login.username has a value, please change your parameter name.");
    
    if (newValue != null && (! (newValue instanceof PropertyToStringDelegate))){
      values.put(this.getFullPropertyName(property), newValue);
    }
    
    
    super.setProperty(property, newValue);
  }

  public Object getProperty(String property) {
//    if (parent != null && (! (parent instanceof PropertyToStringDelegate))){
//      throw new DslException("parameter name : " + this.getFullPropertyName(property) + " is not supported. if you using \".\" in your parameter, there is a ambiguousness for parent child relationship, for example login.username if a valid identifier for us, but when you add login.username.lastname, we have no idea it is going to access a property from identifier login.username or it is a new identifier. ");
//    }
    
    Object value = getPropertyInnernal(property);
    
    if (value == null)
      value = super.getProperty(property);
    
    if (value == null){
      this.setProperty(property, newNextLevelProperty(this, property));
      return super.getProperty(property);
    } else
      return value;
  }
  
  protected PropertyToStringDelegate newNextLevelProperty(PropertyToStringDelegate parent, String propertyName){
    return new PropertyToStringDelegate(parent, propertyName, values);
  }
  
  protected Object getPropertyInnernal(String property){
    Object value = values.get(getFullPropertyName(property));
    return value;
  }
  
  protected String getFullPropertyName(String property){
    if (parent == null)
      return property;
    else{
      if (property == null)
        return parent.getFullPropertyName(propertyName);
      else
        return parent.getFullPropertyName(propertyName + "." + property);
    }
  }
  
  public String getFullPropertyName(){
    return this.getFullPropertyName(null);
  }
  
  protected PropertyToStringDelegate getParent() {
    return parent;
  }
}