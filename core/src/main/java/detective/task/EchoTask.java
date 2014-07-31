package detective.task;

import java.util.Map;

import detective.core.Parameters;

/**
 * Echo all input data
 *
 */
public class EchoTask extends AbstractTask{

  @Override
  protected void doExecute(Parameters config, Parameters output) {
    for (String key : config.keySet()){
      output.put("echotask." + key, config.get(key));
    }
  }

}
