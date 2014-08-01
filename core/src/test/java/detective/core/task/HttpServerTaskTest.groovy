package detective.core.task;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import detective.core.config.ConfigException;
import detective.task.HttpServerTask;
import detective.task.RestTask;

public class HttpServerTaskTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test(expected=ConfigException.class)
  public void testNullConfig() {
    HttpServerTask task = new HttpServerTask();
    task.execute(null);
  }
  
  @Test
  public void testSnoopServer() {
//    HttpServerTask task = new HttpServerTask();
//    Map server = task.execute(["httpserver.port":8081]);
//    try {
//      RestTask clientTask = new RestTask();
//      Map result = clientTask.execute(["rest.url":"http://localhost:8081", "rest.method":"GET"]);
//      
//      assert result.body.length() > 0 ;
//    } finally {
//      server.handler.close();
//    }
  }

}
