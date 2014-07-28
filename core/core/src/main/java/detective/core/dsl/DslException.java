package detective.core.dsl;

public class DslException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public DslException(String msg){
    super(msg);
  }
  
  public DslException(String message, Throwable cause) {
    super(message, cause);
  }

}
