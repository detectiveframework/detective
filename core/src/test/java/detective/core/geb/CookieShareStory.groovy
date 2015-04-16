package detective.core.geb

import static detective.core.Detective.*;
import geb.Browser;
import org.openqa.selenium.Cookie

class CookieSharePage extends GebDetectivePage {
  static url = "https://github.com/detectiveframework"
  
  static at = { title.startsWith("detectiveframework") }  
}

class CookieSharePage2 extends GebDetectivePage {
  static url = "https://github.com/detectiveframework/detective"
  
  static at = { title.startsWith("detectiveframework/detective") }
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
      
      http.cookies.cookies[0].name << "TestGebCookieIntegration"
      http.cookies.cookies[0].value << "TestGebCookieIntegration"
    }
  }
}
