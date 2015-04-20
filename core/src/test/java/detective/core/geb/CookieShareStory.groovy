package detective.core.geb

import static detective.core.Detective.*;
import geb.Browser;
import geb.Page
import org.apache.http.impl.cookie.BasicClientCookie
import org.openqa.selenium.Cookie

class CookieSharePage extends GebDetectivePage {
  static url = "https://github.com/detectiveframework"
  
  static at = { title.startsWith("detectiveframework") }  
}

class CookieSharePage2 extends GebDetectivePage {
  static url = "https://github.com/detectiveframework/detective"
  
  static at = { title.startsWith("detectiveframework/detective") }

  //Before load the page, we setup cookies
  public void beforeLoad(Page previousPage){
    readCookies();
  }
}

story() "Geb Integration Cookie share with HttpClient Story" {
  scenario "Cookie share from Geb to HttpClient"{
    "go to detective github page with geb and add a custom test cookie"{
      browser {
        to CookieSharePage
        
        waitFor {at CookieSharePage }
        
        getDriver().manage().addCookie(new Cookie("TestGebCookieIntegration", "GebIntegration"));
        
        shareCookies();
      }      
    }
    
    "read detective github page with httpclient task, the cookie should bring forward from geb"{
      http.address = "https://github.com/detectiveframework/detective"
      http.use_shared_cookies = true
      runtask httpclientTask();
      
      cookie = http.cookies.cookies.find(){
        it.name == "TestGebCookieIntegration"
      }
      cookie << not (null)
      cookie.name << "TestGebCookieIntegration"
      cookie.value << "GebIntegration"
    }
  }
  
  scenario "Cookie share from HttpClient to Geb"{
    "read detective github page with httpclient task, and create our custom cookie for test"{
      http.address = "https://github.com/detectiveframework"
      http.use_shared_cookies = true
      runtask httpclientTask();
      
      http.cookies.addCookie(new BasicClientCookie("HttpClientCookieItegration", "HttpClientValue"))
    }
    
    "access github with Geb and should able to read the custom cookie we added before"{
      browser {
        to CookieSharePage2
        
        waitFor {at CookieSharePage2 }
        
        readCookies()
      }
      
      cookie = browser.driver.manage().cookies.find(){
        it.name == "HttpClientCookieItegration"
      }
      cookie << not (null)
      cookie.name << "HttpClientCookieItegration"
      cookie.value << "HttpClientValue"
    }
  }
}
