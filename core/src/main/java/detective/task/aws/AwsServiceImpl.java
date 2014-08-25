package detective.task.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;

/**
 * @author James Luo
 * @version 17/12/2012 5:28:42 PM
 */
public class AwsServiceImpl implements InitializingBean, AwsService {

  private static Logger log = LoggerFactory.getLogger(AwsServiceImpl.class);

  private String accessKeyId;

  private String secretKey;

  private AWSCredentialsProvider credentialsProvider = null;

  private AmazonS3 s3 = null;
  private AmazonSQSClient queue = null;
  private AmazonSNSClient sns = null;
  private AmazonDynamoDB dydbv2 = null;

  private String s3Endpoint = null;

  public String getS3Endpoint() {
    return s3Endpoint;
  }

  public void setS3Endpoint(String s3Endpoint) {
    this.s3Endpoint = s3Endpoint;
  }

  private String sqsEndpoint = null;
  private String snsEndpoint = null;
  private String dynamodbEndpoint = null;

  private boolean proxyMode;
  private String proxyHost;
  private int proxyPort;
  private String proxyUsername;
  private String proxyPassword;
  
  public AWSCredentials getCredetials() {
    return this.credentialsProvider.getCredentials();
  }

  private class AWSCredentialsConfigFileProvider implements AWSCredentialsProvider{

    @Override
    public AWSCredentials getCredentials() {
      return new BasicAWSCredentials(accessKeyId, secretKey);
    }

    @Override
    public void refresh() {
      
    }
    
  }
  
  private AWSCredentialsProvider createCredentialsProvider(){
    return new AWSCredentialsProviderChain(
        new InstanceProfileCredentialsProvider(),
        new EnvironmentVariableCredentialsProvider(),
        new SystemPropertiesCredentialsProvider(),
        new AWSCredentialsConfigFileProvider()
        );
  }

  private void factory() {
    ClientConfiguration clientConfig = new ClientConfiguration();
    clientConfig.setProtocol(Protocol.HTTP);

    if (isProxyMode()) {
      log.info("======AWS run in proxy mode." + getProxyHost() + ":" + getProxyPort() + "======");
      System.err.println("======AWS run in proxy mode." + getProxyHost() + ":" + getProxyPort()
          + "======");
      clientConfig.setProxyHost(getProxyHost());
      clientConfig.setProxyPort(getProxyPort());
      if (getProxyUsername() != null && getProxyUsername().length() > 0)
        clientConfig.setProxyUsername(getProxyUsername());
      if (getProxyPassword() != null && getProxyPassword().length() > 0)
        clientConfig.setProxyPassword(getProxyPassword());
    } else {
      log.info("======AWS run in normal mode." + "======");
      System.err.println("======AWS run in normal mode." + "======");
    }

    credentialsProvider = this.createCredentialsProvider();
    
    s3 = new AmazonS3Client(credentialsProvider, clientConfig);
    s3.setEndpoint(s3Endpoint);

    queue = new AmazonSQSClient(credentialsProvider, clientConfig);
    queue.setEndpoint(sqsEndpoint);
    
    sns = new AmazonSNSClient(credentialsProvider, clientConfig);
    sns.setEndpoint(snsEndpoint);
    
    dydbv2 = new AmazonDynamoDBClient(credentialsProvider, clientConfig);
    dydbv2.setEndpoint(dynamodbEndpoint);
  }

  public AmazonS3 getS3() {
    return s3;
  }

  public String getAccessKeyId() {
    return accessKeyId;
  }

  public void setAccessKeyId(String accessKeyId) {
    this.accessKeyId = accessKeyId;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    factory();
  }

  @Override
  public AmazonSQS getQueue() {
    return this.queue;
  }

  public String getSqsEndpoint() {
    return sqsEndpoint;
  }

  public void setSqsEndpoint(String sqsEndpoint) {
    this.sqsEndpoint = sqsEndpoint;
  }

  public String getDynamodbEndpoint() {
    return dynamodbEndpoint;
  }

  public void setDynamodbEndpoint(String dynamodbEndpoint) {
    this.dynamodbEndpoint = dynamodbEndpoint;
  }

  public boolean isProxyMode() {
    return proxyMode;
  }

  public void setProxyMode(boolean proxyMode) {
    this.proxyMode = proxyMode;
  }

  public String getProxyHost() {
    return proxyHost;
  }

  public void setProxyHost(String proxyHost) {
    this.proxyHost = proxyHost;
  }

  public int getProxyPort() {
    return proxyPort;
  }

  public void setProxyPort(int proxyPort) {
    this.proxyPort = proxyPort;
  }

  public String getProxyUsername() {
    return proxyUsername;
  }

  public void setProxyUsername(String proxyUsername) {
    this.proxyUsername = proxyUsername;
  }

  public String getProxyPassword() {
    return proxyPassword;
  }

  public void setProxyPassword(String proxyPassword) {
    this.proxyPassword = proxyPassword;
  }

  @Override
  public AmazonSNS getSns() {
    return sns;
  }

  public String getSnsEndpoint() {
    return snsEndpoint;
  }

  public void setSnsEndpoint(String snsEndpoint) {
    this.snsEndpoint = snsEndpoint;
  }

  @Override
  public AmazonDynamoDB getDydbV2() {
    return dydbv2;
  }

}
