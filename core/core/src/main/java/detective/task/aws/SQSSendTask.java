package detective.task.aws;

import groovy.lang.Closure;

import java.util.Map;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import detective.core.TestTask;
import detective.core.config.ConfigException;
import detective.task.AbstractTask;

/**
 * Send a String to any queue with Region config
 * 
 * @author James Luo
 *
 */
public class SQSSendTask extends AbstractTask implements TestTask{
  
  public static final String sqs_send_queuename = "sqs.send.queuename";

  @Override
  protected void doExecute(Map<String, Object> config, Map<String, Object> output) {
    String accessId = this.readAsString(config, "sqs_accessId", null, false, null);
    String securityKey = this.readAsString(config, "sqs_SecurityKey", null, false, null);
    String queueName = this.readAsString(config, sqs_send_queuename, null, false, "sqs.send.queuename not present in config");
    String dataToSend = this.readAsString(config, "sqs.send.data", null, false, "sqs.send.data not present");
    
    AmazonSQSClient client = new AmazonSQSClient();
    SendMessageRequest request = new SendMessageRequest(); 
    client.sendMessage(request);
  }
  
  
  public static SQSSendTask sqsSendTask(Closure<? extends Object> cl){
    return new SQSSendTask();
  }


}
