package detective.core.dsl.builder;

import detective.core.Parameters;
import detective.core.runner.PropertyToStringDelegate;

public class ShareDataAwardDelegate extends PropertyToStringDelegate{    

  public ShareDataAwardDelegate(Parameters values){
    super(values);
  }
  
  public ShareDataAwardDelegate(PropertyToStringDelegate parent, String propertyName, Parameters values){
    super(parent, propertyName, values);
  }
  
  protected PropertyToStringDelegate newNextLevelProperty(PropertyToStringDelegate parent, String propertyName){
    return new ShareDataAwardDelegate(parent, propertyName, values);
  }
  
  
  protected Object getPropertyInnernal(String property){
    Object value = values.getUnwrappered(getFullPropertyName(property));
    return value;
  }
  
}