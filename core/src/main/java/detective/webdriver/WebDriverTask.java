package detective.webdriver;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import detective.core.Parameters;
import detective.core.TestTask;
import detective.core.config.ConfigException;
import detective.task.AbstractTask;

/**
 * 
 * The task for WebDriver, just a common way to use WebDriver, you should create your own page object 
 * to simplify the creation of a story
 * 
 * <br>Data In:
 * <ul>
 *  <li>browser.url</li>
 *  <li>browser.id.*</li>
 *  <li>browser.name.*</li>
 * </ul>
 * @author James Luo
 *
 */
public class WebDriverTask extends AbstractTask implements TestTask{

  private WebDriver driver;  
  
  @Override
  protected void doExecute(Parameters config, Parameters output) {
    String url = this.readAsString(config, "browser.url", null, false, "the url you want browser navigate to");
    driver.get(url);
  }

  public WebDriver getDriver() {
    return driver;
  }

  public void setDriver(WebDriver driver) {
    this.driver = driver;
  }

}
