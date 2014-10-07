# Introduction

Welcome in detective framework!

[Visit Our Documentation Page](http://detectiveframework.github.io/detective)

```groovy
package detective.core.script

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


