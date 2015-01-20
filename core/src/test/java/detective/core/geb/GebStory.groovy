package detective.core.geb

import static detective.core.Detective.*;

story() "Demostrate Geb" {
  scenario "first result for wikipedia search should be wikipedia"{
    browser {
      to GoogleHomePage
      assert at(GoogleHomePage)
      search.field.value("wikipedia")
      waitFor { at GoogleResultsPage }
      assert firstResultLink.text().startsWith("Wikipedia")
      firstResultLink.click()
      waitFor { at WikipediaPage }
    }
    
    given {
      
//      to GoogleHomePage
//      at GoogleHomePage
    }
    
//    when {
//      search.field.value("wikipedia")
//    }   
//    
//    then {
//      waitFor { at GoogleResultsPage }
//      firstResultLink.text() == "Wikipedia"
//    }
//    
//    when {
//      firstResultLink.click()
//    }
//    
//    then {
//      waitFor { at WikipediaPage }
//    }
  }
}
