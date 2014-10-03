package detective.core.distribute;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import detective.core.services.DetectiveFactory;

public class SparkDriver {

  private final static Logger logger = LoggerFactory.getLogger(SparkDriver.class);
  
  public static void run(String packageName, String... args) {
    String master = DetectiveFactory.INSTANCE.getConfig().getString("spark.master");
    if (args != null && args.length >= 1)
      master = args[0];
    
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
  }
  
}
