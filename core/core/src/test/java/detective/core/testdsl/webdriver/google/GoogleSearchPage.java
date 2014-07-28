package detective.core.testdsl.webdriver.google;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import detective.webdriver.PageObject;

public class GoogleSearchPage implements PageObject {

  // The element is now looked up using the name attribute
  @FindBy(how = How.NAME, using = "q")
  // @FindBy(name = "q")
  private WebElement searchBox;

  public void searchFor(String text) {
    // We continue using the element just as before
    searchBox.sendKeys(text);
    searchBox.submit();
  }

  public WebDriver getDriver() {
    // TODO Auto-generated method stub
    return null;
  }

  public static void main(String[] args) {
    // Create a new instance of a driver
    WebDriver driver = new HtmlUnitDriver();

    // Navigate to the right place
    driver.get("http://www.google.com/");

    // Create a new instance of the search page class
    // and initialise any WebElement fields in it.
    GoogleSearchPage page = PageFactory.initElements(driver, GoogleSearchPage.class);

    // And now do the search.
    page.searchFor("Cheese");
  }

}
