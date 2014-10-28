# Jenkins Integration

## Command Line

detective.core.distribute.SparkDriver applicationName packageOrClassName

you can config master via -Dspark.master=yourMasterUrl

opt/spark/bin/spark-submit --class detective.core.distribute.SparkDriver --master MASTER_URL YOURJARFile ___YourPackageNameOrStoryName___ ___YourApplicationNameWillShowInSparkUI___ -Dspark.master=MASTER_URL

## Jenkins with

Detective r

http://spark.apache.org/docs/0.9.1/configuration.html
