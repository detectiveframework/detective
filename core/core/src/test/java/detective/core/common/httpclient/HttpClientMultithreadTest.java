package detective.core.common.httpclient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;

import detective.common.httpclient.GetThread;
import detective.common.httpclient.IdleConnectionMonitorThread;

public class HttpClientMultithreadTest {

  @Test
  public void test() throws InterruptedException {
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
    
    new IdleConnectionMonitorThread(cm).start();

    // URIs to perform GETs on
    String[] urisToGet =
        {
            "http://www.google.com/", "http://www.yahoo.com/", "http://www.facebook.com/"
        };

    // create a thread for each URI
    GetThread[] threads = new GetThread[urisToGet.length];
    for (int i = 0; i < threads.length; i++) {
      HttpGet httpget = new HttpGet(urisToGet[i]);
      threads[i] = new GetThread(httpClient, httpget);
    }

    // start the threads
    for (int j = 0; j < threads.length; j++) {
      threads[j].start();
    }
    
    

    // join the threads
    for (int j = 0; j < threads.length; j++) {
      threads[j].join();
    }

  }
  
  @Test
  public void testPressure() throws InterruptedException {
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
    
    new IdleConnectionMonitorThread(cm).start();

    PressureThread.pressure(httpClient, 20, "https://sf360test.com.au/login/sf360test");
  }
  
  public static class PressureThread{
    public static void pressure(CloseableHttpClient httpClient, final int threadCount, final String url) throws InterruptedException{
      GetThread[] threads = new GetThread[threadCount];
      for (int i = 0; i < threads.length; i++) {
        HttpGet httpget = new HttpGet(url);
        threads[i] = new GetThread(httpClient, httpget);
      }

      // start the threads
      for (int j = 0; j < threads.length; j++) {
        threads[j].start();
      }

      // join the threads
      for (int j = 0; j < threads.length; j++) {
        threads[j].join();
      }
    }
  }

}
