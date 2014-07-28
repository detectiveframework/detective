package detective.core.runner;

import detective.core.dsl.DslException;

public class WrongPropertyNameInDslException extends DslException{

  private static final long serialVersionUID = 1L;
  
  private final String propertyName;

  public WrongPropertyNameInDslException(String propertyName) {
    super(propertyName);
    this.propertyName = propertyName;
  }

  public String getPropertyName() {
    return propertyName;
  }

}
