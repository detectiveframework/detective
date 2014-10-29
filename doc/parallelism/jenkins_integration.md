# Jenkins Integration

## Command Line

detective.core.distribute.SparkDriver applicationName packageOrClassName

you can config master via -Dspark.master=yourMasterUrl

sudo /opt/spark/bin/spark-submit --class detective.core.distribute.SparkDriver --master [masterURL] --conf "spark.executor.memory=2G" ./six-test-0.5.0-SNAPSHOT.jar [packageName] [applicationName] -Dspark.master=[masterURLAgain]

## Jenkins with

Detective r

http://spark.apache.org/docs/0.9.1/configuration.html
