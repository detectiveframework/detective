package detective.core.dsl.builder;

import groovy.lang.Closure;

import java.util.Map;

import detective.core.Parameters;
import detective.core.Story;
import detective.core.dsl.SharedDataPlaceHolder;
import detective.core.runner.PropertyToStringDelegate;

public class StoryDelegate extends PropertyToStringDelegate{    
  protected Story story;
  protected Closure<?> closure;

  public StoryDelegate(Parameters values){
    super(values);
  }
  
  public StoryDelegate(PropertyToStringDelegate parent, String propertyName, Parameters values){
    super(parent, propertyName, values);
  }
  
  protected PropertyToStringDelegate newNextLevelProperty(PropertyToStringDelegate parent, String propertyName){
    return new StoryDelegate(parent, propertyName, values);
  }
  
  public Object getProperty(String property) {
    return super.getProperty(property);
  }
}