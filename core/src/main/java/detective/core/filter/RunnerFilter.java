package detective.core.filter;

import detective.core.Scenario;
import detective.core.Story;
import detective.core.TestTask;

public interface RunnerFilter {
  
  void doFilterRunStory(Story story, RunnerFilterChain chain);
  
  void doFilterRunScenario(Scenario scenario, RunnerFilterChain chain);
  
  void doFilterRunDataTableRow(Scenario scenario, RunnerFilterChain chain);
  
  void doFilterRunTask(Scenario scenario, TestTask task, RunnerFilterChain chain);

}
