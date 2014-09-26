package detective.core.distribute.scenario;

import detective.core.Scenario;
import detective.core.distribute.JobStoryRunContext;

public class ScenarioRunContext {

  private JobStoryRunContext jobStoryRunContext;
  
  private Scenario scenario;

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
  
}
