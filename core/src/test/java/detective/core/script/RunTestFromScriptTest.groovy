package detective.core.script;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RunTestFromScriptTest {
  
  
  
  @Before
  public void setUp() throws Exception {
    
  }

  @After
  public void tearDown() throws Exception {
    
  }
  

  @Test
  public void testRunTestFromScript() {
    StockStory story = new StockStory();
    story.run();
  }
  
  
}
