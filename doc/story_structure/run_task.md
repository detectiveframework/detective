# Run Task

## Run a specific scenario

If there is a system property called "detective.runner.scenario.index" has been setup, detective will run that scenario only. Please note the index starts from **ZERO**

For example you can config "VM arguments" in your eclipase with "-Ddetective.runner.scenario.index=1"

```groovy
package detective.core.runner

import static detective.core.Detective.*;

System.setProperty("detective.runner.scenario.index", "1");

story() "Run scenario number 1 only" {
  """
    As a developer, I sometimes just want run 1 scenario, you can setup a system property and tell detective
    which scenario you'd like to run.
    In this story scenario0 and scenario2 has a failure check but if you run the whole story, no exception raised
    as we ran scenario1 only
  """

  scenario0 "Scenario 0 which will be ignored" {
    onlystep "all in one step" {
      parameterA = "This is the value"
      runtask echoTask();
      echotask.parameterA << "This is the WRONG!!! value"
    }
  }

  scenario1 "Scenario 1 which will run" {
    onlystep "all in one step" {
      parameterA = "This is the value"
      runtask echoTask();
      echotask.parameterA << "This is the value"
    }
  }

  scenario2 "Scenario 2 which will be ignored" {
    onlystep "all in one step" {
      parameterA = "This is the value"
      runtask echoTask();
      echotask.parameterA << "This is the WRONG!!! value"
    }
  }
}

```

Once you run above script, you will see some log below tell you that 2
```log
[2014-10-09 12:03:02,561] [main] INFO [LogToConsoleFilter:23] - Scenario [Scenario 0 which will be ignored] ignored.
[2014-10-09 12:03:02,565] [main] INFO [LogToConsoleFilter:25] - Scenario [Scenario 1 which will run] succeed.
[2014-10-09 12:03:02,565] [main] INFO [LogToConsoleFilter:23] - Scenario [Scenario 2 which will be ignored] ignored.
[2014-10-09 12:03:02,565] [main] INFO [LogToConsoleFilter:36] - Story [Run scenario number 1 only] ran successfully.
```

