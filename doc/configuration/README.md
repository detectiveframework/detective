# Configuration

All configuration can be read in task as long as you got the right name


## Default Configuration
Detective comes with a default config file which located at classpath://detective-config-default.conf

To view the file content please visite [detective-config-default.conf](https://github.com/detectiveframework/detective/blob/master/core/src/main/resources/detective-config-default.conf)

## Application Configuration
You can create a new configuration file which call "detective.conf" and put it into your classpath. Anything that defined in your conf file will overwrite default configuration file.

## Overwrite from Commend Line
The config can be overwite from commend line for example:
```
java -Dparallel.max_poolsize=300```


##Developer Secion

To read config in your code or task, just invoke getConfig in Detective factory, for example:
```java
Detective.getConfig().getString("parallel.max_poolsize");
```

