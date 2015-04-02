package detective.core.distribute.jobresult;

import detective.core.Parameters;
import detective.task.AbstractTask;

public class TaskAbleToPrintUserMessage extends AbstractTask{

  @Override
  protected void doExecute(Parameters config, Parameters output) {
    this.logMessage("This is the message from task");
  }

}
