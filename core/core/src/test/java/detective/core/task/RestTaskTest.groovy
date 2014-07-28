package detective.core.task;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import detective.core.config.ConfigException;
import detective.task.RestTask;

public class RestTaskTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test(expected=ConfigException.class)
  public void testNullConfig() {
    RestTask task = new RestTask();
    task.execute(null);
  }
  
  @Test(expected=ConfigException.class)
  public void testWrongMethod() {
    RestTask task = new RestTask();
    task.execute(["rest.url":"www.google.com", "method":"GET"]);
  }
  
  @Test
  public void testGetGoogle() {
    RestTask task = new RestTask();
    Map result = task.execute(["rest.url":"http://www.google.com", "rest.method":"GET"]);
    assert result.body.length() > 0;
  }
  
  

}


