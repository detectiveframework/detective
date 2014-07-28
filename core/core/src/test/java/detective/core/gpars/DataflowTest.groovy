package detective.core.gpars;

import static groovyx.gpars.dataflow.Dataflow.task

import static org.junit.Assert.*;

import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.dataflow.DataflowVariable
import groovyx.gpars.dataflow.Dataflows
import org.junit.Test;

public class DataflowTest {

  @Test
  public void dataflow(){
    final def x = new DataflowVariable()
    final def y = new DataflowVariable()
    final def z = new DataflowVariable()
    
    task {
        z << x.val + y.val
    }
    
    task {
        x << 10
    }
    
    task {
        y << 5
    }
    
    assert z.val == 15
    
    println "Result: ${z.val}"
  }
  
  @Test
  public void dataflowSimpleVersion(){
    final def df = new Dataflows()
    
    task {
        df.z = df.x + df.y
    }
    
    task {
        df.x = 10
    }
    
    task {
        df.y = 5
    }
    
    assert df.z == 15
    
    println "Result: ${df.z}"
  }
  
  @Test
  public void dataflowQueue(){
    def words = ['Groovy', 'fantastic', 'concurrency', 'fun', 'enjoy', 'safe', 'GPars', 'data', 'flow']
    final def buffer = new DataflowQueue()
    
    task {
        for (word in words) {
            buffer << word.toUpperCase()  //add to the buffer
        }
    }
    
    task {
        while(true) println buffer.val  //read from the buffer in a loop
    }
    
    println buffer.val
  }
  
  
}
