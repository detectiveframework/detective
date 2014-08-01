package detective.task;

import java.util.Map;
import java.util.Set;

import detective.core.Parameters;
import detective.core.dsl.SharedVariable;

/**
 * Echo all input data
 * 
 * <h4>input</h4>
 * isEchoUnbindShareVariables, by default is true, please note this may cause thread pause all the time
 *
 */
public class EchoTask extends AbstractTask{

  @Override
  protected void doExecute(Parameters config, Parameters output) {
//    Set<String> unbindedKeys = config.getUnbindShareVarKeys();
    
    for (String key : config.keySet()){
//      if (unbindedKeys.contains(key))
//        continue;//Have to do the check otherwise will cause thread pause all the time
      
      Object obj = config.getUnwrappered(key);
      output.put("echotask." + key, obj);
    }
  }

}
