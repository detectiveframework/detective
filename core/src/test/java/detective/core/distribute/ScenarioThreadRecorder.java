package detective.core.distribute;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScenarioThreadRecorder {
  
  private static Map<String, Map<String, String>> datas = new ConcurrentHashMap<String, Map<String, String>>();
  
  public static String recordThread(String testCaseName, String scenarioOrStoryName){
    String thread = Thread.currentThread().getName();
    
    if (datas.containsKey(testCaseName))
      datas.get(testCaseName).put(scenarioOrStoryName, thread);
    else{
      Map<String, String> data = new HashMap<String, String>();
      data.put(scenarioOrStoryName, thread);
      datas.put(testCaseName, data);
    }
    return thread;
  }
  
  public static void remove(String key){
    datas.remove(key);
  }
  
  public static String getThread(String testCaseName, String scenarioOrStoryName){
    if (datas.containsKey(testCaseName))
      return datas.get(testCaseName).get(scenarioOrStoryName);
    else
      return null;
  }

}
