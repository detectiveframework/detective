package detective.task.aws;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
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
public class S3ListTask extends AbstractTask implements TestTask{
  
  @Override
  protected void doExecute(Parameters config, Parameters output) {
    String bucketName = this.readAsString(config, "aws.s3.bucketName", null, false, "aws.s3.bucketName not present in config");
    String prefix = this.readAsString(config, "aws.s3.list.prefix", null, false, "aws.s3.list.prefix not present");
    
    AmazonS3 client = new AmazonS3Client(AwsUtils.getCredentialProviderC(config), AwsUtils.getClientConfig(config));
    client.setRegion(AwsUtils.getRegion(config));
    
    ListObjectsRequest listObjectsRequest =
        new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix);

    List<S3ObjectSummary> summarys = new ArrayList<S3ObjectSummary>();
    ObjectListing objectListing;
    do {
      objectListing = client.listObjects(listObjectsRequest);
      summarys.addAll(objectListing.getObjectSummaries());
      String marker = objectListing.getNextMarker();
      listObjectsRequest.setMarker(marker);
      
      Detective.info("Reading from S3...current marker:" + marker + " Continue..." );
    } while (objectListing.isTruncated());
    
    output.put("s3ObjectSummaries", summarys);
  }
  
  public static S3ListTask s3ListTask(Closure<? extends Object> cl){
    return new S3ListTask();
  }


}
