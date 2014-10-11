# JUnit

you can embed all detective stories into a JUnit test.

please create a Groovy junit file

```groovy
  @Test
  public void testEchoTask(){
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
  }
```

##Access Variables outside story

```groovy
  private AtomicInteger runningCounter = new AtomicInteger(0);

  @Test
  public void testDataTableParamBatchAdd() {
    story() "DataTable support batch add" {
      inOrderTo "generate a lot of data table records"

      scenario_refund "Refunded items should be returned to stock" {
        scenarioTable {
          sweater.black | sweater.refund.black  | expect.sweater.balck
          3             | 1                     | 4
          1             | 10                    | 11
          100           | 50                    | 150
        }

        (1..200).each { number ->
          scenarioTable {
            sweater.black | sweater.refund.black  | expect.sweater.balck
            number        | 2                     | number + 2
          }
        }

        given "a list of black sweaters left in stock and customer returns the sweaters for a refund" {
          sweater.blue = 0
          //sweater.refund.black = 1 //Data Table always overwrite individual parameter
          this.runningCounter.andIncrement;

          runtask TestTaskFactory.stockManagerTask()
        }

        then "I should have expected black sweaters in stock"{
          println Thread.currentThread().getName() + " sweater.black=${sweater.black} sweater.refund.black = ${sweater.refund.black}  expect.sweater.balck = ${expect.sweater.balck} Done, ${sweater.black} + ${sweater.refund.black} = ${expect.sweater.balck}"
          sweater.black << equalTo(expect.sweater.balck)
          assert this.runningCounter.get() == 203
        }
      }
    }
  }
```

