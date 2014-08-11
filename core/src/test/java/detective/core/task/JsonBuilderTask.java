package detective.core.task;

import groovy.json.JsonSlurper;
import detective.core.Parameters;
import detective.task.AbstractTask;

public class JsonBuilderTask extends AbstractTask{

  @Override
  protected void doExecute(Parameters config, Parameters output) {
    String json = this.readAsString(config, "jsoninput", null, false, "jsoninput is missing");
    output.put("json", new JsonSlurper().parseText(json));
  }

}
