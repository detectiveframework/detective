package detective.core.geb

import static detective.core.Detective.*;
import geb.Browser;

story() "Geb page with detective integration" {
  scenario "url replacement and parameter exchange"{
    "go to google home page"{
      //http://google.com/ncr?parameter1=?&parameter2=defaultValue&defaultValueWillBeOverwritten=defaultButOverwritten
      parameter1 = "ThisIsParameter1"
      defaultValueWillBeOverwritten = "ThisIsValueFromStory"
      
      browser {
        to GoogleHomePageDetective
        assert at(GoogleHomePageDetective)
        //The url will replaced as below but we can't test at this moment
        //assert currentUrl == "http://google.com/ncr?parameter1=ThisIsParameter1&parameter2=defaultValue&defaultValueWillBeOverwritten=ThisIsValueFromStory"
      }
      
      //The browser will fill title with current page title
      //and search with current search value
      title << "Google"
      search << ""
    }
    
    "let's search wikipedia" {
      browser {
        searchFor "wikipedia"
        waitFor { at GoogleResultsPageDetective }
        assert firstResultLink.text().startsWith("Wikipedia")
      }
      
      results << not(null)
      results[0] << not(null)
      firstResultLink.text().startsWith("Wikipedia") << true
    }
  }
}
