# Effect the Output

You are able to customise the output while run stories.

If you run your test in a console, or any shell that [ANSI escape code](http://en.wikipedia.org/wiki/ANSI_escape_code), you wil get a colorred output.

You can install a plugin to make eclipse support ANSI Console
https://github.com/mihnita/ansi-econsole
http://www.mihai-nita.net/eclipse


```groovy
package detective.core.story.output

import static detective.core.Detective.*;

story() "Demo for how story and task effect output" {

  scenario "A passed simple case" {
    given "a parameter out side of scenario table" {
      parameter.given1 = "given1"
      runtask echoTask()
    }

    then "log infomration out to console"{
      logMessage("This message will print out to console with bold style")
      exampleTable = table {
        rowIndex  |col1    | col2    | expected  | msg
        0         |1       | 2       | 3         | "msg1"
        1         |4       | 5       | 9         | "msg2"
       }
      logMessage("You can show table as well")
      logMessage(exampleTable)
    }

    then "It will run echo parameter.given1"{
      echotask.parameter.given1 << "given1"
    }
  }

  scenario "One table not a subset of other table" {
    given "a table with col1, col2, and expected" {
      myOriginTable = table {
        col1    | col2    | expected  | msg
        1       | 2       | 3         | "msg1"
        4       | 5       | 9         | "msg2-Wrong"
        10      | 11      | 21        | "msg3"
       }

      myExpectedTable = table {
        rowIndex  |col1    | col2    | expected  | msg
        0         |1       | 2       | 3         | "msg1"
        1         |4       | 5       | 9         | "msg2"
        2         |10      | 11      | 21        | "msg3"
       }

    }

    then "this scearnio will fail and console should print out two tables"{
      myExpectedTable << subsetOf(myOriginTable)
    }
  }
}
```

After clone this repository and run below command
```
mvn -f pom.xml -DMAVEN_OPTS="-Xmx1024m" compile exec:java -Dexec.args="detective.core.story.output.EffectOutputDemoStory"
```

you will have below results

![Result in Console](/asserts/EffectOutput.png)

