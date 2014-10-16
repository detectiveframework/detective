package detective.core.distribute;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import detective.core.distribute.collector.JobCollector;
import detective.core.services.DetectiveFactory;

public class SparkDriver {

  private final static Logger logger = LoggerFactory.getLogger(SparkDriver.class);
  
  /**
   * ./bin/spark-submit --class detective.core.distribute.SparkDriver --master spark://127.0.0.1:7000 your.jar spark://127.0.0.1:7000
   * 
   * @param args classOrPackageName
   * @return
   */
  public static int main(String[] args) {
    if (args == null || args.length < 1){
      System.err.println("Usage: detective.core.distribute.SparkDriver packageOrClassName \n"
          + "  if you'd like to setup master, please change your detective.conf file or -Dspark.master=yourMasterUrl");
      return -1;
    }
    
    String master = DetectiveFactory.INSTANCE.getConfig().getString("spark.master");
    
    String packageName = args[0];
    
    SparkConf sparkConf = new SparkConf()
      .setAppName("Detective-" + packageName)
      .setMaster(master)
      //.setMaster("local[8]")
      //.setMaster("spark://127.0.0.1:7000")
      .set("spark.driver.host", "localhost")
      //.set("spark.driver.port", "5555")
      ;
    JavaSparkContext jsc = new JavaSparkContext(sparkConf);

    List<Job> jobs = JobCollector.collectAll(packageName);
    JavaRDD<Job> dataset = jsc.parallelize(jobs);
    
    dataset.foreach(new VoidFunction<Job>(){

      @Override
      public void call(Job job) throws Exception {
        logger.info(job.toString());
        
        JobRunner runner = new JobRunnerFilterImpl();
        runner.run(job);
      }});
    
    return 0;
  }
  


}
