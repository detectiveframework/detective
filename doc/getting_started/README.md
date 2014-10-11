# Getting Started

A story structure
```groovy
import static detective.core.Detective.*;

story() "Your Descritpion for this story" {
  scenario [Option Your Description for this scenario] {
    scenarioTable | [Option verb, for example given, when, then] "Your description for this step"{
      parameterName = "your value"
      runtask taskName()
    }

    step*
  }

  scenario*
}
```

##HelloWorld
```groovy
import static detective.core.Detective.*;

story() "Demostrate hello world" {
  scenario {
    step1 "print out hello world"{
      println "HelloWorld"
    }
  }
}
```

print out a word is not a test, let's do something useful
```groovy
story() "Demostrate test string concat" {
  scenario "String concat" {
    give "word Hello"{
      word1 = "Hello"
    }
    give "word World"{
      word2 = "World";
    }
    when "run String.concat"{
      helloworld = word1.concat(word2)
    }
    then "word1 and word2 should concated"{
      helloworld << "HelloWorld"
    }
  }
}
```

If you change expected in "then" section from "HelloWorld" to "HelloWorldWrong", you will see some exception in your log.

```log
Caused by: java.lang.AssertionError:
Expected: "HelloWorldWrong"
     got: <HelloWorld>
```

##Task

A story must friendly to testers / BAs and Project Managers, introduce task is a good idea.
A task normally created by a developer, it usually represent a business function, for example a web page, a web service.

In last example "word1.concat(word2)" is too hard for tester.

In our guide we will use a task called "echoTask" a lot, it only echo back all parameters pass to it by add "echotask." inforent.

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

