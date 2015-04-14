package detective.core.geb

import static detective.core.Detective.*;
import geb.Browser;

story() "Demostrate Geb" {
  scenario "invoke geb by browser closure"{
    "go to google home page by closure"{
      //http://google.com/ncr?parameter1=?&parameter2=defaultValue&defaultValueWillBeOverwritten=defaultButOverwritten
      parameter1 = "ThisIsParameter1"
      defaultValueWillBeOverwritten = "ThisIsValueFromStory"
      
      browser {
        to GoogleHomePageDetective
        assert at(GoogleHomePageDetective)
        //assert currentUrl == "http://google.com/ncr?parameter1=ThisIsParameter1&parameter2=defaultValue&defaultValueWillBeOverwritten=ThisIsValueFromStory"
      }
      
      //The browser will fill title with current page title
      //and search with current search value
      title << "Google"
      search << ""
    }
    
    "let's search wikipedia" {
      browser {
        search.field.value("wikipedia")
        waitFor { at GoogleResultsPageDetective }
        assert firstResultLink.text().startsWith("Wikipedia")
      }
      
      results << not(null)
      results[0] << not(null)
      firstResultLink.text().startsWith("Wikipedia") << true
    }
  }
}
