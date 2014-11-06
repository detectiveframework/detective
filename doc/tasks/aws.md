# aws

## Credentials

we created a provider list which read from Instance Profile first, then environment variable, then System properties and last one is via Detective config file.

```java
  private AWSCredentialsProvider createCredentialsProvider(){
    return new AWSCredentialsProviderChain(
        new InstanceProfileCredentialsProvider(),
        new EnvironmentVariableCredentialsProvider(),
        new SystemPropertiesCredentialsProvider(),
        new AWSCredentialsConfigFileProvider()
        );
  }
```

A full configuration

```json
aws {
	#protocol : HTTPS
	protocol : HTTP
	region   : US_WEST_1
	#GovCloud US_EAST_1 US_WEST_1 US_WEST_2 EU_WEST_1 AP_SOUTHEAST_1 AP_SOUTHEAST_2 AP_NORTHEAST_1 SA_EAST_1
	credentials {
		instanceProfile 	: Yes
		environmentVariable : Yes
		systemProperties 	: Yes
		accessKey           : "YOUR_ACCESS_KEY"
		secretKey           : "YOUR_SECRET_KEY"
	}
	proxy {
		host 	 : ""
      	port 	 : 80
      	username : ""
      	password : ""
   }
}
```

