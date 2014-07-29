package detective.core;

import java.util.List;
import java.util.Map;

public interface Story {

  String getTitle();

  String getRole();

  String getFeature();

  String getBenefit();
  
  /**
   * This map can be changed out side of the story, for example in task which running inside other thread
   * @return
   */
  Map<String, Object> getSharedDataMap();
  void putSharedData(String key, Object value);

  List<Scenario> getScenarios();

  List<Scenario> getBeforeTasks();
  List<Scenario> getAfterTasks();
}