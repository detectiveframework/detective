package detective.core.dsl;

/**
 * This class is for internal use only, please don't use it in dsl, no test case has been written for that.
 * 
 * @author James Luo
 *
 * @param <T>
 */
public interface SharedVariable<T> {

  /**
   * Represents a thread-safe single-assignment, multi-read variable.
   * Each instance of DataflowVariable can be read repeatedly any time using the 'val' property and assigned once
   * in its lifetime using the '&lt;&lt;' operator. Reads preceding assignment will be blocked until the value
   * is assigned.
   */
  T getValue();
  
  
  void setValue(T value);
  
  /**
   * Check if value has been set already for this expression
   *
   * @return true if bound already
   */
  boolean isBound();
  
}
