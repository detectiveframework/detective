package detective.core.task.webdriver;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.ScreenshotException;

public class WebDriverTest {
  
  @Before
  public void setupEnv(){
    System.setProperty("webdriver.chrome.driver", "/Users/bglcorp/git/detective/core/core/src/main/resources/chromedrivers/mac/chromedriver");
    System.setProperty(org.openqa.selenium.phantomjs.PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/Users/bglcorp/git/detective/core/core/src/main/resources/phantomjs/phantomjs-1.9.7-macosx/bin/phantomjs");
    
  }

  public String extractScreenShot(WebDriverException e) {
    Throwable cause = e.getCause();
    if (cause instanceof ScreenshotException) {
      return ((ScreenshotException) cause).getBase64EncodedScreenshot();
    }
    return null;
  }
  
  protected File takeScreenShot(WebDriver webdriver) throws IOException{
    File scrFile = ((TakesScreenshot)webdriver).getScreenshotAs(OutputType.FILE);
    // Now you can do whatever you need to do with it, for example copy somewhere

    File destFile = new File("/Users/bglcorp/git/detective/core/core/target/classes/" + scrFile.getName());
    FileUtils.copyFile(scrFile, destFile);
    
    return destFile; 
  }

  @Test
  public void testGoogleWithFirefoxDriver() {
    // The Firefox driver supports javascript
    WebDriver driver = new FirefoxDriver();

    test(driver);

    driver.quit();
  }

  private AtomicInteger threadRunns = new AtomicInteger(0);
  
  /**
   * 20 max
   * @throws InterruptedException
   */
  @Test
  public void testLocalChromeDriver() throws InterruptedException {
    runMultipleThread(20, new WebDriverFactory(){

      public WebDriver getDriver() {
        return new ChromeDriver();
      }
      
    });
  }

  //@Test
  public void testRemoteChrome() throws InterruptedException {
    runMultipleThread(1, new WebDriverFactory(){

      public WebDriver getDriver() {
        return new RemoteWebDriver(DesiredCapabilities.chrome());
      }
      
    });    
  }
  
  //@Test
  public void testRemotePhantomjs() throws InterruptedException {
    runMultipleThread(1, new WebDriverFactory(){

      public WebDriver getDriver() {
        return new RemoteWebDriver(DesiredCapabilities.phantomjs());
      }
      
    });
  }
  
  /**
   * 60 Max
   * 40 looks stable
   * @throws InterruptedException
   */
  @Test
  public void testLocalPhantomjs() throws InterruptedException {
    runMultipleThread(60, new WebDriverFactory(){

      public WebDriver getDriver() {
        return new PhantomJSDriver();
      }
      
    } );
  }

  public static interface WebDriverFactory{
    WebDriver getDriver();
  }
  
  private void runMultipleThread(final int threadCount, final WebDriverFactory driverFactory) throws InterruptedException {
    threadRunns = new AtomicInteger(0);
    for(int i = 0; i < threadCount; i++){
      Thread thread = new Thread(new Runnable(){

        public void run() {
          try {
            WebDriver driver = driverFactory.getDriver();
            System.out.println("Startup " + driver.toString() + " - " + threadRunns);
            
            test(driver);
            
            System.out.println("Done " + driver.toString() + " - " + threadRunns);
            
            File file = takeScreenShot(driver);
            //Runtime.getRuntime().exec("open " + file.getAbsolutePath());
            
            driver.quit();
          } catch (Exception e){
            e.printStackTrace();
          }finally{
            threadRunns.incrementAndGet();
          }
          
        }});
      thread.start();
    }  
    
    while(true){
      Thread.sleep(2000);
      System.out.println("Waiting " + threadRunns);
      if (threadRunns.get() >= threadCount )
        return;
    }
  }

  //@Test
  public void testRemoteFirefox() {
    WebDriver driver = new RemoteWebDriver(DesiredCapabilities.firefox());

    // Query the driver to find out more information
    Capabilities actualCapabilities = ((RemoteWebDriver) driver).getCapabilities();

    test(driver);

    driver.quit();
  }

  private void test(WebDriver driver) {
    // And now use it
    driver.get("http://www.google.com/webhp?complete=1&hl=en");
    // Enter the query string "Cheese"
    WebElement query = driver.findElement(By.name("q"));
    query.sendKeys("Cheese");

    // Sleep until the div we want is visible or 5 seconds is over
    long end = System.currentTimeMillis() + 5000;
    while (System.currentTimeMillis() < end) {
      WebElement resultsDiv = driver.findElement(By.className("gssb_e"));

      // If results have been returned, the results are displayed in a drop down.
      if (resultsDiv.isDisplayed()) {
        break;
      }
    }

    // And now list the suggestions
    List<WebElement> allSuggestions = driver.findElements(By.xpath("//td[@class='gssb_a gbqfsf']"));

    for (WebElement suggestion : allSuggestions) {
      System.out.println(suggestion.getText());
    }
  }

}
