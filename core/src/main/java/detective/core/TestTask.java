package detective.core;

import java.util.Map;

import com.typesafe.config.Config;

import detective.core.config.ConfigException;

public interface TestTask {

  Parameters execute(Parameters config) throws ConfigException;
  
}
