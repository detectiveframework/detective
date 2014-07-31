package detective.task;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;

import detective.core.Parameters;
import detective.core.TestTask;
import detective.core.config.ConfigException;
import detective.core.dsl.DslException;
import detective.core.dsl.ParametersImpl;

public abstract class AbstractTask implements TestTask{

  @Override
  public Parameters execute(Parameters config) throws ConfigException {
    if (config == null)
      throw ConfigException.configCantEmpty();
    
    Parameters output = new ParametersImpl();
    
    doExecute(config, output);
    
    return output;
  }
  
  protected abstract void doExecute(Parameters config, Parameters output);
  
  protected String readAsString(Parameters config, String key, String defaultValue, boolean isOptional, String errorWhenNotPresent){
    Object value = config.get(key);
    if (value == null){
      if (! isOptional)
        throw new ConfigException(errorWhenNotPresent);
      else
        return defaultValue;
    }else
      return value.toString();
  }
  
  protected Long readAsLong(Parameters config, String key, Long defaultValue, boolean isOptional, String errorWhenNotPresent){
    String str = readAsString(config, key, null, isOptional, errorWhenNotPresent);
    if (str == null)
      return null;
    
    return Long.valueOf(str);
  }
  
  protected <T extends Object> T readAsObject(Parameters config, String key, T defaultValue, boolean isOptional, String errorMsg, Class<T> clazz){
    Object value = config.get(key);
    if (value == null){
      if (! isOptional){
        throw new ConfigException(errorMsg);
      }else
        return defaultValue;
    }else{
      return clazz.cast(value);
    }
  }
  
  protected <T extends Object> T readOptional(Parameters config, String key, T defaultValue, Class<T> clazz){
    if (!config.containsKey(key))
      return null;
    
    Object value = config.get(key);
    if (value == null){
      throw new DslException("null optional have to setup as Optional.absent()");
    }else{
      if (value instanceof Optional){
        if (((Optional)value).isPresent())
          return clazz.cast(((Optional)value).get());
        else
          return null;
      }else
        return clazz.cast(value);
    }
  }

}

