package detective.task.aws;

import groovy.lang.Closure;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import detective.core.Detective;
import detective.core.Parameters;
import detective.core.TestTask;
import detective.task.AbstractTask;

/**
 * Get information from a S3 bucket, prefix is optional.
 * 
 * @author James Luo
 *
 */
public class S3GetTask extends AbstractTask implements TestTask{
  
  @Override
  protected void doExecute(Parameters config, Parameters output) {
    String bucketName = this.readAsString(config, "aws.s3.bucketName", null, false, "aws.s3.bucketName not present in config");
    String key = this.readAsString(config, "aws.s3.get.key", null, false, "aws.s3.get.key not present");
    String versionId = this.readAsString(config, "aws.s3.get.versionId", null, true, "aws.s3.get.versionId not present");
    
    AmazonS3 client = new AmazonS3Client(AwsUtils.getCredentialProviderC(config), AwsUtils.getClientConfig(config));
    client.setRegion(AwsUtils.getRegion(config));
    
    String keyInfo = "[" + bucketName + "] " + key;
    while (true){
      try{
        Detective.info("Reading from S3: " + keyInfo);
        S3Object object = null;
        
        if (versionId == null){
          object = client.getObject(
              new GetObjectRequest(bucketName, key));
        }else{
          object = client.getObject(
              new GetObjectRequest(bucketName, key, versionId));
        }
        
        InputStream objectData = object.getObjectContent();
        try{
          output.put("content", IOUtils.toByteArray(objectData));
          output.put("metadata", object.getObjectMetadata());
        }finally{
          try {
            objectData.close();
          } catch (IOException e) {
            throw new RuntimeException("Error to close s3 input stream." + e.getMessage(), e);
          }
        }
        
        break;
      }catch (AmazonS3Exception e){
        Detective.error("Error when read details from S3 for dynamic module:" + keyInfo, e);
        break;
      }catch (Throwable e){
        Detective.error("Error when read details from S3 for dynamic module, will wait 2 seconds and retry..."  + keyInfo, e);
        
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e1) {
          Detective.error(e1.getMessage(), e1);
        }
      }
    }
  }
  
  public static S3GetTask s3GetTask(Closure<? extends Object> cl){
    return new S3GetTask();
  }


}
