# Geb Integration

we support geb by provide browser cloure, you can run everything that geb supported into that cloure, for example [Link](https://github.com/detectiveframework/detective/blob/master/core/src/test/java/detective/core/geb/GebBasicStory.groovy):

```groovy
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

```

You can access [PageObject](http://www.gebish.org/manual/current/pages.html#pages) as usual, [full link here](https://github.com/detectiveframework/detective/blob/master/core/src/test/java/detective/core/geb/GebDetectiveStory.groovy).

```groovy
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
```

## Configuration

```
browser{
	# The driver, could be htmlunit/firefox/ie/chrome
	default_driver : firefox
	# Report, could be everyPage / testFailureOnly / disable
	report : everyPage
	reportsDir : target/browser-reports
}
```

