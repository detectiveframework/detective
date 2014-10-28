# Spark integration

By default Detective has been configed to connect spark master "local[200]", which means you don't need config anything, a new spark "cluster" will start up in your local machine with 200 threads capacity.

## Build a true Spark cluster

To build your own spark cluster, please reference to [Spark documentation ](https://spark.apache.org/docs/latest/cluster-overview.html)

however, we require you do one change as we have to use a newer HttpClient component but Spark shipped a older one. To overcome this issue, for now please make a change in


