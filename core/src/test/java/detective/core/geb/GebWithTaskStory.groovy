package detective.core.geb

import static detective.core.Detective.*;
import geb.Browser;

story() "Demostrate Geb" {
  scenario "invoke geb by browser closure"{
    "go to google home page by closure"{
      browser {
        to GoogleHomePage
        assert at(GoogleHomePage)
        search.field.value("wikipedia")
        waitFor { at GoogleResultsPage }
        assert firstResultLink.text().startsWith("Wikipedia")
        firstResultLink.click()
        waitFor { at WikipediaPage }
        report "wikipedia"
      }
    }
  }
  
  scenario "invoke geb by browser. "{    
    "go to google home page step by step"{
      browser.to GoogleHomePage
      browser.at(GoogleHomePage)
      browser.search.field.value("wikipedia")
      browser.waitFor { browser.at GoogleResultsPage }
      browser.firstResultLink.text().startsWith("Wikipedia")
      browser.firstResultLink.click()
      browser.waitFor { browser.at WikipediaPage }
    }
  }
  
  scenario "invoke get by browser()" {    
    "go to google home page step by step"{
      browser() to GoogleHomePage
      browser() at(GoogleHomePage)
      browser().search.field.value("wikipedia")
      browser().waitFor { browser().at GoogleResultsPage }
      browser().firstResultLink.text().startsWith("Wikipedia")
      browser().firstResultLink.click()
      browser().waitFor { browser().at WikipediaPage }
    }
  }
  
  scenario "create exception and should create a report for that"{
    "go to google home page by closure"{
      expect("title.endsWith \"Google Search\"") {
        browser {
          to GoogleHomePage
          assert at(GoogleResultsPage)
        }
      }
    }
  }
}
