package detective.task.aws;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import detective.core.Parameters;

public class AwsUtils {
  
  private static class AWSCredentialsConfigFileProvider implements AWSCredentialsProvider{
    
    private final String secretKey;
    private final String accessId;
    
    public AWSCredentialsConfigFileProvider(String accessId, String secretKey){
      this.accessId = accessId;
      this.secretKey = secretKey;
    }

    @Override
    public AWSCredentials getCredentials() {
      return new BasicAWSCredentials(accessId, secretKey);
    }

    @Override
    public void refresh() {
      
    }
    
  }

  public static ClientConfiguration getClientConfig(Parameters config){
    ClientConfiguration clientConfig = new ClientConfiguration();
    clientConfig.setProtocol(Protocol.valueOf(config.get("aws.protocol").toString().toUpperCase()));
    
    String host = config.get("aws.proxy.host").toString();
    if (host != null && host.length() > 0){
      clientConfig.setProxyHost(host);
      clientConfig.setProxyPort(Integer.valueOf(config.get("aws.proxy.port").toString()));
      
      String username = config.get("aws.proxy.username").toString();
      String password = config.get("aws.proxy.password").toString();
      
      if (username != null && username.length() > 0)
        clientConfig.setProxyUsername(username);
      if (password != null && password.length() > 0)
        clientConfig.setProxyPassword(password);
    }
    return clientConfig;
  }
  
  public static AWSCredentialsProvider getCredentialProviderC(Parameters config){
    List<AWSCredentialsProvider> providers = new ArrayList<AWSCredentialsProvider>();
    
    if ("YES".equals(config.get("aws.credentials.instaneProfile")))
      providers.add(new InstanceProfileCredentialsProvider());
    if ("YES".equals(config.get("aws.credentials.environmentVariable")))
      providers.add(new EnvironmentVariableCredentialsProvider());
    if ("YES".equals(config.get("aws.credentials.systemProperties")))
      providers.add(new SystemPropertiesCredentialsProvider());
    
    String accessKey = config.get("aws.credentials.accessKey").toString();
    String secretKey = config.get("aws.credentials.secretKey").toString();
    
    if (accessKey != null && accessKey.length() > 0){
      providers.add(new AWSCredentialsConfigFileProvider(accessKey, secretKey));
    }
    
    return new AWSCredentialsProviderChain(providers.toArray(new AWSCredentialsProvider[0]));
  }
  

  public static Region getRegion(Parameters config) {
    return Region.getRegion(Regions.valueOf(config.get("aws.region").toString().toUpperCase()));
  }
  
  
}
