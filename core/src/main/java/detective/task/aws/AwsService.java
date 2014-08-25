package detective.task.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sqs.AmazonSQS;

/** 
 * @author James Luo
 */
public interface AwsService {

  AmazonS3 getS3();
  
  //SQS
  AmazonSQS getQueue();
  
  //SNS
  AmazonSNS getSns();
  
  AmazonDynamoDB getDydbV2();
  
  AWSCredentials getCredetials();
    
}
