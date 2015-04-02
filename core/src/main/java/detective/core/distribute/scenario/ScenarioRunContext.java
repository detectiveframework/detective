package detective.core.distribute.scenario;

import java.util.ArrayList;
import java.util.List;

import detective.core.Scenario;
import detective.core.distribute.JobRunResult.JobRunResultSteps;
import detective.core.distribute.JobStoryRunContext;

public class ScenarioRunContext {

  private JobStoryRunContext jobStoryRunContext;
  
  private Scenario scenario;
  
  private List<JobRunResultSteps> steps = new ArrayList<JobRunResultSteps>();

  public JobStoryRunContext getJobStoryRunContext() {
    return jobStoryRunContext;
  }

  public void setJobStoryRunContext(JobStoryRunContext jobStoryRunContext) {
    this.jobStoryRunContext = jobStoryRunContext;
  }

  public Scenario getScenario() {
    return scenario;
  }

  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }
  
  public void addJobRunResultSteps(JobRunResultSteps step){
    steps.add(step);
  }

  public List<JobRunResultSteps> getSteps() {
    return steps;
  }
  
}
