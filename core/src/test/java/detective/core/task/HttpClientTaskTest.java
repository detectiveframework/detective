package detective.core.task;

import detective.task.HttpClientTask;
import junit.framework.Assert;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.lang.reflect.Method;
import java.nio.charset.Charset;

import static junit.framework.Assert.*;

/**
 * Created by amila on 11/05/2016.
 */
public class HttpClientTaskTest extends HttpClientTask {

  /**
   * Tests the protected method using reflection
   * @throws Exception
   */
  @Test public void testAddBasicAuthentication() throws Exception {
    String userName = "testuser";
    String password = "testpass";
    String auth = userName + ":" + password;
    byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("UTF-8")));
    String expectedAuthString = "Basic " + new String(encodedAuth);

    HttpUriRequest request = new HttpPost("http://www.google.com.au");

    HttpClientTask httpClientTask = new HttpClientTask();
    Method method = httpClientTask.getClass().getDeclaredMethod("addBasicAuthentication", HttpUriRequest.class, String.class, String.class);
    method.setAccessible(true);
    method.invoke(httpClientTask, request, userName, password);

    Header[] headers = request.getHeaders("Authorization");
    assertNotNull(headers);
    assertTrue(headers.length > 0);
    Header authHeader = headers[0];
    String authHeaderValue = authHeader.getValue();

    assertEquals(expectedAuthString, authHeaderValue);

  }

}
