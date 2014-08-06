package detective.core.dsl;

/**
 * A interface that identify the object is a wrapper of other object
 * 
 * @author James Luo
 *
 * @param <T>
 */
public interface WrappedObject<T> {

  T getValue();
  
  
  void setValue(T value);
  
}
