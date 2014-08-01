package detective.core.dsl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import detective.core.Story;
import groovyx.gpars.dataflow.DataflowVariable;

public class SharedVariableImpl<T> extends DataflowVariable<T> implements SharedVariable<T>{
  
  private static final Logger logger = LoggerFactory.getLogger(SharedVariableImpl.class);
  
  private final String varName;
  private final Story story;
  
  public SharedVariableImpl(Story story, String name){
    this.story = story;
    this.varName = name;
  }

  @Override
  public T getValue() {
    try {
      if (! this.isBound()){
        logger.info("Value [ " + varName + " ] in story [" + story.getTitle() + "] is waiting for a value, current thread [" + Thread.currentThread().getName() + "] is paused.");
      }
      return this.get();
    } catch (Throwable e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
  
  @Override
  public void setValue(T value) {    
    this.leftShift(value);
    logger.info("Value [ " + varName + " ] in story [" + story.getTitle() + "] has been setup by [" + Thread.currentThread().getName() + "]");
  }

  
}
