package detective.core.geb

import static detective.core.Detective.*;
import geb.Browser;

class UrlParameterAutoFillPage extends GebDetectivePage {
    static url = "https://github.com/{path1}/{path2}?parameter1=?&paramWithDefaultValue=defaultValue&paramWillOverwrite=originValue"
  
    static at = { title.contains("detectiveframework/detective") }  
  }

story() "Geb Integration URL auto fill Story" {
  scenario "fill url parameters with detective parameter system"{
    "go to detective github page"{
      path1 = "detectiveframework"
      path2 = "detective"
      parameter1 = "thisIsValueOfParameter1"
      paramWillOverwrite = "valueFromStory"
      browser {
        to UrlParameterAutoFillPage
        
        waitFor {at UrlParameterAutoFillPage }
        
        assert driver.currentUrl == "https://github.com/detectiveframework/detective?parameter1=thisIsValueOfParameter1&paramWithDefaultValue=defaultValue&paramWillOverwrite=valueFromStory"
      }      
    }
  }
}
