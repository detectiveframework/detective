# expect

In some cases, you are **expecting** some error will happen.

```groovy
package detective.core.story.matcher

import static detective.core.Detective.*;


story() "Demo how to expect an error" {

  scenario "expect with a error message" {
    given "a parameter" {
      parameterA = "This is the value"
    }

    when "run echo task"{
      runtask echoTask();
    }

    then "parameters will echo back"{
      echotask.parameterA << "This is the value"
    }

    and "below check should throw an error which message contains \"WRONG value \""{
      expect("WRONG value!") {
        echotask.parameterA << "WRONG value!"
      }
    }

    and "below check should throw an error which message contains \"WRONG value \" and \"This is the value\""{
      expect(["WRONG value!", "This is the value"]){
        echotask.parameterA << "WRONG value!"
      }
    }
  }
}

```
