package detective.core.geb;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import detective.core.Detective;
import detective.core.Parameters;
import detective.core.exception.StoryFailException;
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
  }  

  @Override
  public void to(Map params, Object[] args) {
    try {
      params = prepareUrlParameters(prepareUrlParameters(params));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    
    super.to(params, args);
  }
  
  
  Map prepareUrlParameters(Map params) throws MalformedURLException, UnsupportedEncodingException{
    URL url = new URL(super.getPageUrl());
    Map<String, String> queries = splitQuery(url);
    Parameters ps = this.getParametersInner();
    for (String key : queries.keySet()){
      if (ps.containsKey(key) && ps.get(key) != null)
        queries.put(key, ps.get(key).toString());
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
  
  protected static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
    Map<String, String> query_pairs = new LinkedHashMap<String, String>();
    String query = url.getQuery();
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
      return urlStr.substring(0, urlStr.indexOf("?"));
    }else
      return urlStr;
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
