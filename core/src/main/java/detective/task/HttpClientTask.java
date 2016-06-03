package detective.task;

import groovy.lang.GString;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import com.amazonaws.HttpMethod;

import detective.common.annotation.ThreadSafe;
import detective.core.Parameters;
import detective.core.TaskException;
import detective.core.config.ConfigException;
import detective.core.services.DetectiveFactory;

/**
 * 
 * HTTP Client Task<br>
 * <p>
 * Support GET, PUT, POST, DELETE, HEAD and OPTIONS
 * 
 * <h4>Input</h4>
 * <pre>
 *   http.use_shared_cookies: default true, identify if this httpclient will read cookies from share data section
 *   http.cookies: optional, output of other HTTPClientTask
 *   http.address: a String or a Java URI
 *   http.method: GET, PUT, POST, DELETE, HEAD, OPTIONS, optional, default to POST
 *   
 *   http.post.string: optional, the data as a plain text which will post to server
 *   
 *   http.post.file.filename: optional, the file you'd like to upload
 *     
 * </pre>
 * <h4>Output</h4>
 * <pre>
 *   http.cookies: the http context current task have (may created by this task or passed in from input)
 *   http.output
 *   http.status.code
 *   http.header.* : all headers returned from server
 *   get
 *     
 * </pre>
 * 
 * <ul>
 *  <li>
 *    GET  The HTTP GET method is defined in section 9.3 of
 * <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>:
 * The GET method means retrieve whatever information (in the form of an
 * entity) is identified by the Request-URI.
 *  </li>
 *  <li>
 *    PUT HTTP PUT method.
 * The HTTP PUT method is defined in section 9.6 of
 * <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>:
 * The PUT method requests that the enclosed entity be stored under the
 * supplied Request-URI.
 *  </li>
 *  <li>
 *    POST HTTP POST method.
 * The HTTP POST method is defined in section 9.5 of
 * <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>:
 * The POST method is used to request that the origin server accept the entity
 * enclosed in the request as a new subordinate of the resource identified by
 * the Request-URI in the Request-Line.
 *  </li>
 *  <li>
 *    DELETE HTTP DELETE method
 * The HTTP DELETE method is defined in section 9.7 of
 * <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>:
 * The DELETE method requests that the origin server delete the resource
 * identified by the Request-URI.
 *  </li>
 *  <li>
 *    HEAD HTTP HEAD method.
 * The HTTP HEAD method is defined in section 9.4 of
 * <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>:
 * The HEAD method is identical to GET except that the server MUST NOT
 * return a message-body in the response.
 *  </li>
 *  <li>
 *  OPTIONS HTTP OPTIONS method.
 * The HTTP OPTIONS method is defined in section 9.2 of
 * <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>:
 *  The OPTIONS method represents a request for information about the
 *  communication options available on the request/response chain
 *  identified by the Request-URI.
 *  </li>
 *  
 * </ul>
 * 
 * 
 * 
 * It able to share cookies which means you can share session between different requests in most cases.
 * 
 * 
 * HttpClient Connection Manager Document Link: http://hc.apache.org/httpcomponents-client-4.3.x/tutorial/html/connmgmt.html
 * 
 * </p>
 * 
 * @author James Luo
 *
 */
@ThreadSafe
public class HttpClientTask extends AbstractTask{
  
  public static final String PARAM_HTTP_COOKIES = "http.cookies";

  public enum HttpMethod {
    GET, POST, PUT, DELETE, HEAD, OPTIONS, TRACE, PATCH;
}


  @Override
  protected void doExecute(Parameters config, Parameters output) {
    CloseableHttpClient httpClient = DetectiveFactory.INSTANCE.getHttpClient();
    
    CookieStore cookieStore = null;
    boolean useSharedCookies = this.readAsString(config, "http.use_shared_cookies", "true", true, null).equals("true");
    if (useSharedCookies){
      cookieStore = this.readOptional(config, PARAM_HTTP_COOKIES, null, CookieStore.class);  
    }
    if (cookieStore == null){
      cookieStore = new BasicCookieStore();
    }
    HttpClientContext context = HttpClientContext.create();
    context.setCookieStore(cookieStore);
    context.setRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).build());

    String method = this.readAsString(config, "http.method", "post", true, null);
    HttpMethod m = HttpMethod.valueOf(method.toUpperCase());
    Object address = this.readAsObject(config, "http.address", null, false, "please provider address your request send to", Object.class);
    URI uri = null;
    if (address instanceof String)
      uri = URI.create((String)address);
    else if (address instanceof GString)
      uri = URI.create(address.toString());
    else if (address instanceof URI)
      uri = (URI)address;
    else
      throw new ConfigException("the address you provided have to be a String or java URI, however your type is " + address.getClass().getName());
    
    HttpUriRequest request = this.createHttpUriRequest(m, uri);
    request.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");

    String userName = this.readAsString(config, "http.auth.username", null, true, "Adding basic http header auth to request");
    String password = this.readAsString(config, "http.auth.password", null, true, "");
    if (null != userName && null != password) {
      request = addBasicAuthentication(request, userName, password);
    }

    String authorizationHeader = this.readAsString(config, "http.auth.header", null, true, "No authorization header found, skipping token auth");
    if (authorizationHeader != null) {
      request = addAuthorizationHeader(request, authorizationHeader);
    }

    if (request instanceof HttpEntityEnclosingRequestBase){
      if (config.containsKey("http.post.string")){
        Object postText = this.readOptional(config, "http.post.string", null, Object.class);
        setupPostRequest((HttpEntityEnclosingRequestBase)request, postText.toString());
      }else if (config.containsKey("http.post.file.filename")){
        String filename = this.readOptional(config, "http.post.file.filename", null, String.class);
        this.setupPostWithFileName((HttpPost)request, filename);
      }
    }
    
    try {
      CloseableHttpResponse response = httpClient.execute(request, context);
      try {
        output.put(PARAM_HTTP_COOKIES, cookieStore);
        output.put("http.protocal.name", response.getStatusLine().getProtocolVersion().getProtocol());
        output.put("http.protocal.version.major", response.getStatusLine().getProtocolVersion().getMajor());
        output.put("http.protocal.version.minor", response.getStatusLine().getProtocolVersion().getMinor());
        output.put("http.protocal.description", response.getStatusLine().getProtocolVersion().toString());
        output.put("http.status.code", response.getStatusLine().getStatusCode());
        output.put("http.status.reason", response.getStatusLine().getReasonPhrase());
        
        //Headers
        for (Header header : response.getAllHeaders()){
          output.put("http.header." + header.getName(), header.getValue());
        }
        
        //Cookies
        //output.put("http.cookies", context.getCookieStore().getCookies());
        
        //Entity
        HttpEntity entity = response.getEntity();
        // add by George Zeng, for adding a content back to do more actions
        Scanner scanner = new Scanner(entity.getContent());
        StringBuilder content = new StringBuilder();
        while(scanner.hasNext()) {
        	content.append(scanner.nextLine()).append("\n");
        }
        scanner.close();
        output.put("http.content", content.toString());
        output.put("http.content.string", content.toString());
        output.put("http.content.length", entity.getContentLength());
        if (entity.getContentEncoding() != null)
          output.put("http.content." + entity.getContentEncoding().getName(), entity.getContentEncoding().getValue());
        if (entity.getContentType() != null)
          output.put("http.content." + entity.getContentType().getName(), entity.getContentType().getValue());
        
        //System.out.println(entity.toString());
      } finally {
        response.close();
      }
    } catch (ClientProtocolException ex) {
      throw new TaskException(ex);
    } catch (IOException ex) {
      throw new TaskException(ex);
    }
  }
  
  protected HttpEntityEnclosingRequestBase setupPostRequest(HttpEntityEnclosingRequestBase request, String postText){
    if (postText == null || postText.length() == 0)
      return request;
    
    StringEntity entity = new StringEntity(postText, "UTF-8");
    BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE,"application/json");
    //request.getParams().setBooleanParameter("http.protocol.expect-continue", false);
    entity.setContentType(basicHeader);
    request.setEntity(entity);
    
    return request;
  }
  
  protected HttpPost setupPostWithFileName(HttpPost request, String fileName){
    if (fileName == null || fileName.length() == 0)
      return request;
    
    
    ContentBody fileBody = new FileBody(new File(fileName), ContentType.DEFAULT_BINARY);
    
    MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
    multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setBoundary("boundaryABC--WebKitFormBoundaryGMApUciYGfDuPa49");
    multipartEntity.addPart("file", fileBody);
    request.setEntity(multipartEntity.build());
    
//    MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, "boundaryABC--WebKitFormBoundaryGMApUciYGfDuPa49", Charset.forName("UTF-8"));
//    mpEntity.addPart("userfile", fileBody);
//    request.setEntity(mpEntity);
    
//    FileEntity entity = new FileEntity(new File(fileName), ContentType.APPLICATION_OCTET_STREAM);
//    BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE,"application/json");
    //request.getParams().setBooleanParameter("http.protocol.expect-continue", false);
//    entity.setContentType(basicHeader);
    
//    request.setEntity(mpEntity);
    
    return request;
  }
  
  /**
   * Create a Commons HttpMethodBase object for the given HTTP method and URI specification.
   * @param httpMethod the HTTP method
   * @param uri the URI
   * @return the Commons HttpMethodBase object
   */
  protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
    switch (httpMethod) {
      case GET:
        return new HttpGet(uri);
      case DELETE:
        return new HttpDeleteWithBody(uri);
      case HEAD:
        return new HttpHead(uri);
      case OPTIONS:
        return new HttpOptions(uri);
      case POST:
        return new HttpPost(uri);
      case PUT:
        return new HttpPut(uri);
      case TRACE:
        return new HttpTrace(uri);
      case PATCH:
        return new HttpPatch(uri);
      default:
        throw new IllegalArgumentException("Invalid HTTP method: " + httpMethod);
    }
  }

  protected HttpUriRequest addBasicAuthentication(HttpUriRequest request, String user, String pass) {

    String auth = user + ":" + pass;
    byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("UTF-8")));
    String authHeader = "Basic " + new String(encodedAuth);
    request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

    return request;
  }

  protected HttpUriRequest addAuthorizationHeader(HttpUriRequest request, String headerValue) {
    request.setHeader(HttpHeaders.AUTHORIZATION, headerValue);
    return request;
  }

}
