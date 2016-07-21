package detective.core.dsl;

/**
 * A interface that identify the object is a wrapper of other object
 * 
 * @author James Luo
 */
public interface WrappedObject<T> {

  /**
   * Get the underline object, please note this may still return a WrappedObject
   * 
   * @return the underline object, please note this may still return a WrappedObject
   */
  T getValue();
  
  
  void setValue(T value);
  
}
