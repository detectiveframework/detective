package detective.core.geb;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.openqa.selenium.Cookie;

import detective.core.Detective;
import detective.core.Parameters;
import detective.core.exception.StoryFailException;
import detective.task.HttpClientTask;
import geb.Page;
import groovy.lang.Closure;

/**
 * Features this class added:
 * <ul>
 * <li>Fill parameters from detective parameter system automatically, 
 * for example google.co?local=en&additional=? the ? mark will be filled automatically first
 * if there is a parameter called "additional" exists. For parameter "local", the value "en" is default setup, it 
 * still can be overwrite by this class if "local" exists in parameter list</li>
 * </ul>
 * 
 * @author james
 *
 */
public class GebDetectivePage extends Page{
  
  private String urlWithoutQuery = null;
  
  /**
   * Get page url without the query pages as we are going to replace it with our parameters
   */
  public String getPageUrl() {
    return getUrlWithoutQuery();
  }
  
  /**
   * Return the page url which defined in url static content
   * @return
   */
  public String getOriginPageUrl(){
    return super.getPageUrl();
  }
  
  public void onLoad(Page previousPage) {
    updateParameters();
    reportIfNeeded();
    
    afterLoad(previousPage);
  }
  
  /**
   * Lifecycle method called when the page is connected to the browser.
   * <p>
   * This implementation does nothing.
   * <p>
   * You can still use onLoad but please remember call super.onLoad(previousPager) there
   *
   * @param previousPage The page that was active before this one
   */
  public void afterLoad(Page previousPage){
    
  }
  
  /**
   * Lifecycle method called when the page about to load, driver has been initialized.
   * <p>
   * This implementation does nothing.
   * <p>
   *
   * @param previousPage The page that was active before this one
   */
  public void beforeLoad(){
    
  }
  
  /**
   * Share the cookies with HttpClientTask
   */
  public void shareCookies(){
    Object store = getCookieStore();
    if (store == null){
      store = new BasicCookieStore();
      this.getParametersInner().put(HttpClientTask.PARAM_HTTP_COOKIES, store);
    }
    
    CookieStore cookieStore = (CookieStore)store; 
    for (Cookie cookie : this.getDriver().manage().getCookies()){
      BasicClientCookie newCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
      newCookie.setDomain(cookie.getDomain());
      newCookie.setPath(cookie.getPath());
      newCookie.setExpiryDate(cookie.getExpiry());
      newCookie.setSecure(cookie.isSecure());
      cookieStore.addCookie(newCookie);
    }
  }
  
  /**
   * Read cookies from Detective parameter system, the cookies usually created by HttpClientTask.
   * As selenium only allow setup for current active domain, we have to check domain name
   */
  public void readCookies(){
    Object store = getCookieStore();
    if (store != null){
      String domainName = getCurrentDomainName();
      if (domainName == null)
        return;
      
      CookieStore cookieStore = (CookieStore)store; 
      for (org.apache.http.cookie.Cookie cookie : cookieStore.getCookies()){
        if (domainName.equalsIgnoreCase(cookie.getDomain()))
          this.getDriver().manage().addCookie(new Cookie(cookie.getName(), cookie.getValue()));
      }       
    }
  }

  protected CookieStore getCookieStore() {
    Object store = this.getParametersInner().get(HttpClientTask.PARAM_HTTP_COOKIES);
    if (store == null)
      return null;
    return (CookieStore)store;
  }
  
  /**
   * return the current opened domain name
   * @return null if there is no page currently open
   */
  private String getCurrentDomainName(){
    String domainName = this.getDriver().getCurrentUrl();
    try {
      if (domainName != null && domainName.length() > 0)
        domainName = new URL(domainName).getHost();
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
    return domainName;
  }

  @Override
  public void to(Map params, Object[] args) {
    beforeLoad();
    
    try {
      params = prepareUrlParameters(prepareUrlParameters(params));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    
    super.to(params, args);
  }
  
  
  Map prepareUrlParameters(Map params) throws MalformedURLException, UnsupportedEncodingException{
    String url = super.getPageUrl();
    Map<String, String> queries = splitQuery(url);
    Parameters ps = this.getParametersInner();
    for (String key : queries.keySet()){
      if (ps.containsKey(key) && ps.get(key) != null)
        queries.put(key, ps.get(key).toString());
      
      if ("?".equals(queries.get(key)))
        queries.put(key, "");
    }
    queries.putAll(params);
    return queries;
  }
  
  /**
   * When a page fully loaded (at check returns true),
   * this method can be invoked, you can read anything from the page
   * and write into detective parameter system
   */
  protected void putParameter(String key, Object value){
    GebSession.getParameters().put(key, value);
  }
  
  /**
   * Get parameters from detective framework
   * @return the Parameters in whole scenario
   */
  protected Parameters getParametersInner(){
    return GebSession.getParameters();
  }
  
  protected static Map<String, String> splitQuery(String url) throws UnsupportedEncodingException {
    Map<String, String> query_pairs = new LinkedHashMap<String, String>();
    if (url.indexOf("?") < 0 & url.length() > 1)
      return query_pairs;
    String query = url.substring(url.indexOf("?") + 1, url.length());
    if (query == null)
      return query_pairs;
    
    String[] pairs = query.split("&");
    for (String pair : pairs) {
        int idx = pair.indexOf("=");
        query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
    }
    return query_pairs;
  }
  
  private String getUrlWithoutQuery(){
    String urlStr = super.getPageUrl();
    if (urlStr.indexOf("?") >= 0){
      urlStr = urlStr.substring(0, urlStr.indexOf("?"));
    }
    
    //Handle path parameters, see test UrlParameterAutoFillPage
    return replaceOneParameter(urlStr, this.getParametersInner());
  }
  
  private String replaceOneParameter(String url, Parameters parameters){
    int indexFirst = url.indexOf("{");
    int indexLast = url.indexOf("}");
    
    if (indexFirst < 0 && indexLast < 0)
      return url;
    else if (indexFirst >= 0 && indexLast < 0){
      throw new RuntimeException("found { in url path but not }");
    }else if (indexFirst < 0 && indexLast >= 0){
      throw new RuntimeException("found } in url path but not {");      
    }else{
      String parameterName = url.substring(indexFirst + 1, indexLast);
      if (! parameters.containsKey(parameterName))
        throw new RuntimeException(parameterName + " not exists in secario parameter list.");
      else{
        url = url.replace("{" + parameterName + "}", parameters.get(parameterName).toString());
        return replaceOneParameter(url, parameters);
      }
    }
  }
  
  private void updateParameters(){
    Method fieldParams;
    try {
      fieldParams = this.getClass().getMethod("getParameters");
    } catch (Exception e) {
      fieldParams = null;
    }
    
    if (fieldParams == null)
      return;
    
    if (fieldParams.getReturnType() == Object.class){
      try {
        Closure<?> closure = (Closure<?>)fieldParams.invoke(this);
        if (closure != null){
          closure = (Closure<?>)closure.clone();
          closure.setDelegate(this);
          closure.setResolveStrategy(Closure.DELEGATE_FIRST);
          Object result = closure.call();
          if (result != null && result instanceof Map){
            Map params = (Map)result;
            for (Object key : params.keySet()){
              this.putParameter(key.toString(), params.get(key));
            }
          }
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      
    }else{
      throw new RuntimeException("parameters have to be a cloure.");
    }  
  }
  
  private void reportIfNeeded(){
    if ("everyPage".equals(Detective.getConfig().getString("browser.report"))){
      this.getBrowser().report(this.getClass().getSimpleName());
    }
  }
}
