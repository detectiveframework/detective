# Filters (TODO)

## Default filter

You can customise what detective do when run a story.

we have 3 different filters

while you run a groovy script directly (in eclipse, from command line, etc), detective will read a runner chain factory from config "runner.normal.filter_chain_factory", by default this set as "detective.core.runner.DefaultRunnerFilterChainFactory" which just simply delegate to "SimpleStoryRunner"

please see code below
```java
public DefaultRunnerFilterChainFactory(){
    List<RunnerFilter<Story>> filters = new ArrayList<RunnerFilter<Story>>();
    filters.add(new SimpleStoryRunnerFilter());
    filters.add(new LogToConsoleFilter());
    chain = new RunnerFilterChainImpl<Story>(filters);
  }
```

## Distributed Filter

we choose Spark as our job distributor, but you can replace to any other framework.

Distribute Filter split to two parts
1. Job collect
2. Distribute Runner

```
| User      | Job Collect    | Distribute Runner

```

