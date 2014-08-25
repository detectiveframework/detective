package detective.task.aws;

import groovy.lang.Closure;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import detective.core.Parameters;
import detective.core.TestTask;
import detective.task.AbstractTask;

/**
 * Send a String to any queue with Region config
 * 
 * @author James Luo
 *
 */
public class SQSSendTask extends AbstractTask implements TestTask{
  
  @Override
  protected void doExecute(Parameters config, Parameters output) {
    String queueUrl = this.readAsString(config, "aws.sqs.queueUrl", null, false, "aws.sqs.queueUrl not present in config");
    String dataToSend = this.readAsString(config, "aws.sqs.sendData", null, false, "aws.sqs.sendData not present");
    
    AmazonSQS client = new AmazonSQSClient(AwsUtils.getCredentialProviderC(config), AwsUtils.getClientConfig(config));
    client.setRegion(AwsUtils.getRegion(config));
    SendMessageRequest request = new SendMessageRequest(queueUrl, dataToSend); 
    client.sendMessage(request);
  }
  
  
  public static SQSSendTask sqsSendTask(Closure<? extends Object> cl){
    return new SQSSendTask();
  }


}
