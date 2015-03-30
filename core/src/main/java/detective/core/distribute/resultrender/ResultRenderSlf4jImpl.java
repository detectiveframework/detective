package detective.core.distribute.resultrender;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import detective.core.distribute.JobRunResult;

public class ResultRenderSlf4jImpl implements ResultRender{
  
  private final static Logger logger = LoggerFactory.getLogger(ResultRenderSlf4jImpl.class);

  @Override
  public void render(List<JobRunResult> jobsAfterRun, long timeElapsedSec) {
    Collections.sort(jobsAfterRun);
    long errors = 0;
    long skipped = 0;
    for (JobRunResult job : jobsAfterRun){
      if (! job.getSuccessed())
        errors = errors + 1;
      if (job.isIgnored())
        skipped ++;
      
      logger.info(job.toString());      
    }
    
    //Long timeElapsedSec = TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
    logger.info("Tests run: "+jobsAfterRun.size()+", Errors: "+errors+", Skipped: "+skipped+", Time elapsed: " + timeElapsedSec + " Seconds ");
    
  }

}
