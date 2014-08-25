package detective.task.aws;

import groovy.lang.Closure;

import java.io.ByteArrayInputStream;
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
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
public class S3PutTask extends AbstractTask implements TestTask{
  
  @Override
  protected void doExecute(Parameters config, Parameters output) {
    String bucketName = this.readAsString(config, "aws.s3.bucketName", null, false, "aws.s3.bucketName not present in config");
    String key = this.readAsString(config, "aws.s3.put.key", null, false, "aws.s3.list.prefix not present");
    
    AmazonS3 client = new AmazonS3Client(AwsUtils.getCredentialProviderC(config), AwsUtils.getClientConfig(config));
    client.setRegion(AwsUtils.getRegion(config));
    ObjectMetadata metadata = new ObjectMetadata();

//    metadata.addUserMetadata(REFER_MODULES,
//        setToString(codeModule.getReferModuleNames()));

    byte[] bytes = (byte[]) config.get("aws.s3.put.content");
    
    PutObjectRequest request = new PutObjectRequest(bucketName,
        key, new ByteArrayInputStream(bytes), metadata);

    client.putObject(request);
  }
  
  public static S3PutTask s3PutTask(Closure<? extends Object> cl){
    return new S3PutTask();
  }


}
