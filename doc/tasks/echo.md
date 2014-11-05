# echo

echoTask only echos back all parameters pass to it by add "echotask." in front.

```groovy
package detective.core.story

import static detective.core.Detective.*;

story() "Simple Story with Echo Task" {
  inOrderTo "demostrate simple story"

  scenario "Echo parameters back" {
    given "a parameter" {
      parameterA = "This is the value"
    }

    when "run echo task"{
      runtask echoTask();
    }

    then "parameters will echo back"{
      echotask.parameterA << "This is the value"
    }
  }
}

```

