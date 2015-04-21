package detective.core.story.groovyassert

import static detective.core.Detective.*;

story() "Simple Story with Echo Task and assert" {
  inOrderTo "demostrate simple story with groovy assert"
  
  scenario "Echo parameters back" {
    given "a parameter" {
      parameterA = "This is the value"
      
      expect("ExpectObjectWrapperWrapper cannot be cast to java.lang.Boolean"){
        //This will generate a error as we wrapped everything, we need review
        //Source Object detective.core.runner.ExpectObjectWrapperWrapper.invokeMethod(String name, Object args)
        assert parameterA == "This is wrong"
      }
    }
    
    when "run echo task"{
      runtask echoTask();
    }
    
    then "parameters will echo back"{
      echotask.parameterA << "This is the value"
    }
  }
}
