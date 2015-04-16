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

## URL Parameter auto-fill

bellow story will pass
```groovy
package detective.core.geb

import static detective.core.Detective.*;
import geb.Browser;

class UrlParameterAutoFillPage extends GebDetectivePage {
    static url = "https://github.com/detectiveframework/detective?parameter1=?&paramWithDefaultValue=defaultValue&paramWillOverwrite=originValue"

    static at = { title.startsWith("detectiveframework/detective") }
  }

story() "Geb Integration URL auto fill Story" {
  scenario "fill url parameters with detective parameter system"{
    "go to detective github page"{
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

```


## Configuration overview

```
browser{
	# The driver, could be htmlunit/firefox/ie/chrome
	default_driver : firefox
	# Report, could be everyPage / testFailureOnly / disable
	report : everyPage
	reportsDir : target/browser-reports
}
```

##Browser support
you can switch between browsers by config **browser.default_driver**
```
	# The driver, could be htmlunit/firefox/ie/chrome
	browser.default_driver : firefox
```


## Screen shot and html dom files
We will create screen shot and html dom for every page with out you call report. the files will save into target/browser-reports folder.

To adjust the behavior, you can change this behavior by
```
    # Report, could be everyPage / testFailureOnly / disable
	report : everyPage
	reportsDir : target/browser-reports
```

## Driver and binaries
we included a [web driver binary manager](https://github.com/bonigarcia/webdrivermanager) and your binaries will config automatically while detective starts.

While detecitve starts you will see log file below
```
[2015-04-15 10:39:03,035] [main] INFO [BrowserManager:124] - Connecting to http://chromedriver.storage.googleapis.com/ to check lastest chromedriver release
[2015-04-15 10:39:04,269] [main] INFO [BrowserManager:112] - Latest driver version: 2.9
[2015-04-15 10:39:04,271] [main] INFO [Downloader:56] - Binary driver previously downloaded /Users/abc/.m2/repository/webdriver/chromedriver/mac32/2.9/chromedriver
[2015-04-15 10:39:04,272] [main] INFO [Downloader:60] - Exporting webdriver.chrome.driver as /Users/abc/.m2/repository/webdriver/chromedriver/mac32/2.9/chromedriver
[2015-04-15 10:39:04,272] [main] INFO [BrowserManager:46] - Connecting to https://api.github.com/repos/operasoftware/operachromiumdriver/releases to check lastest OperaDriver release
[2015-04-15 10:39:06,026] [main] INFO [BrowserManager:57] - Latest driver version: 0.2.1
[2015-04-15 10:39:06,028] [main] INFO [BrowserManager:124] - Connecting to http://selenium-release.storage.googleapis.com/ to check lastest IEDriverServer release
[2015-04-15 10:39:06,989] [main] INFO [BrowserManager:112] - Latest driver version: 2.45
```

