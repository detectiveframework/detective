package detective.core.geb

import static detective.core.Detective.*;
import geb.Browser;




story() "Geb Script Basic Story" {
  scenario "invoke geb by browser closure"{
    "go to google home page by closure"{
      browser {
        go "http://google.com/ncr"
         
        assert title == "Google"
         
        $("input", name: "q").value("Wikipedia")
      }
      
      //you can also direct access geb via browser property
      browser.go "http://google.com/ncr"
    }
  }
}
