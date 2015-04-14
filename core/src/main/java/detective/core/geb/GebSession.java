package detective.core.geb;

import detective.core.Detective;
import detective.core.Parameters;
import geb.Browser;

/**
 * This class will used for share browser instance between all steps in ONE scenario
 * The Browser will bind into a Thread, when a story try to access geb functionality
 * the instance will created by invoke "getBrowser", once the scenario stops, 
 * cleanBrowser will be invoked.
 * 
 * This apply to Parameters as well
 * 
 * @author james
 *
 */
public class GebSession {

  private static ThreadLocal<Browser> browser = new ThreadLocal<Browser>();
  private static ThreadLocal<Parameters> parameters = new ThreadLocal<Parameters>();
  
  private static Browser newBrowser(){
    Browser browser = new Browser();
    browser.setBaseUrl(Detective.getConfig().getString("global.baseurl"));
    return browser;
  }
  
  public static boolean isBrowserAvailable(){
    return browser.get() != null;
  }
  
  public static synchronized Browser getBrowser(){
    if (browser.get() == null){
      browser.set(newBrowser());
    }
    
    return browser.get(); 
  }
  
  public static void cleanBrowser(){
    if (browser.get() == null)
      return;
    
    try {
      getBrowser().close();
    } finally {
      browser.set(null);
    }    
  }
  
  public static boolean isParametersAvailable(){
    return parameters.get() != null;
  }
  
  public static Parameters getParameters(){
    if (parameters.get() == null)
      throw new RuntimeException("Parameters need setup first");
    
    return parameters.get();
  }
  
  public static synchronized Parameters setParameters(Parameters realParameters){
    parameters.set(realParameters);
    
    return parameters.get();
  }
  
  public static void cleanParameters(){
    parameters.set(null);       
  }
}
