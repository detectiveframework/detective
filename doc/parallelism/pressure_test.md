# Pressure Test

```
spark.pressureTest.duplicateTasks=100
```

Above config will run 100 time for every scenario, depands on your running environment.

For a spark cluster, all scenarios will distrubute to all slave machine.

In you local server, this is depends on your config "parallel.max_poolsize", it will try to use all thread pool.
