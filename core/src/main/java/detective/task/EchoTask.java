package detective.task;

import java.util.Map;

/**
 * Echo all input data
 *
 */
public class EchoTask extends AbstractTask{

  @Override
  protected void doExecute(Map<String, Object> config, Map<String, Object> output) {
    for (String key : config.keySet()){
      output.put("echotask." + key, config.get(key));
    }
  }

}
