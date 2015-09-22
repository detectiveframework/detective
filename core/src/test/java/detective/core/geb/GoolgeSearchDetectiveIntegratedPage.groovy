package detective.core.geb

import geb.Browser
import geb.Page
import geb.Module
import detective.core.geb.GebDetectivePage;

// modules are reusable fragments that can be used across pages that can be parameterised
// here we are using a module to model the search function on the home and results pages
class GoolgeSearchModuleDetective extends Module {

  // a parameterised value set when the module is included
  def buttonValue

  // the content DSL
  static content = {

    // name the search input control “field”, defining it with the jQuery like navigator
    field { $("input", name: "q") }

    // the search button declares that it takes us to the results page, and uses the
    // parameterised buttonValue to define itself
    button(to: GoogleResultsPage) {
      $("input", value: buttonValue)
    }
  }
}

class GoogleHomePageDetective extends GebDetectivePage {

  // pages can define their location, either absolutely or relative to a base
  static url = "http://google.com/ncr?parameter1=?&parameter2=defaultValue&defaultValueWillBeOverwritten=defaultButOverwritten"

  // “at checkers” allow verifying that the browser is at the expected page
  static at = { title == "Google" }

  static content = {
    // include the previously defined module
    search { module GoolgeSearchModuleDetective, buttonValue: "Google Search" }    
  }
  
  static parameters = {
    [
      "search": search.field.value(),
      "title": title
    ]
  }
}

class GoogleResultsPageDetective extends GebDetectivePage {
  static at = { title.endsWith "Google Search" }
  static content = {
    // reuse our previously defined module
    search { module GoolgeSearchModuleDetective, buttonValue: "Search" }

    // content definitions can compose and build from other definitions
    results { $("#search .g") }
    result { i -> results[i] }
    resultLink { i -> result(i).find(".r > a") }
    firstResultLink { resultLink(0) }
  }
  static parameters = {
    [
      "results" : results,
      "firstResultLink" : firstResultLink
    ]
  }
}

class WikipediaPageDetective extends GebDetectivePage {
  static at = { title.startsWith("Wikipedia") }
}
