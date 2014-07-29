package detective.task;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.typesafe.config.Config;

import detective.core.TestTask;
import detective.core.config.ConfigException;

/**
 * This task support all rest calls
 * <br>Data In:
 * <ul>
 *  <li>rest.domain</li>
 *  <li>rest.url</li>
 *  <li>rest.method (case insensitive and one of following: DELETE / GET / HEAD / OPTIONS / POST /PUT)</li>
 *  <li>rest.post.data</li>
 *  <li>rest.post.data.file</li>
 * </ul>
 * @author James Luo
 *
 */
public class RestTask implements TestTask {
  
  protected final MultiThreadedHttpConnectionManager connectionManager = 
      new MultiThreadedHttpConnectionManager();
  protected final HttpClient client = new HttpClient(connectionManager);
  
  protected final RestTemplate rest = new RestTemplate();

  public Map<String, Object> execute(Map<String, Object> config) throws ConfigException {
    if (config == null)
      throw new ConfigException("Config can't be null");
    
    Object method = config.get("rest.method");
    if (method == null)
      throw new ConfigException("Config rest.method not present, it can be one of DELETE / GET / HEAD / OPTIONS / POST /PUT");
    
    String body = null;
    if (method.toString().equalsIgnoreCase("GET")){
      ResponseEntity<String> entity = rest.getForEntity(config.get("rest.url").toString(), String.class);
      body = entity.getBody();
    }
    
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("body", body);
    return result;
  }
  
  private void byHttpClient(){
//    GetMethod get = new GetMethod("http://httpcomponents.apache.org/");
//    try {
//        client.executeMethod(get);
//        // print response to stdout
//        System.out.println(get.getResponseBodyAsStream());
//    } finally {
//        // be sure the connection is released back to the connection 
//        // manager
//        get.releaseConnection();
//    }
  }
  
  
}
