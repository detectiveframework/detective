package detective.core;

import java.util.Map;

import com.typesafe.config.Config;

import detective.core.config.ConfigException;

public interface TestTask {

  Map<String, Object> execute(Map<String, Object> config) throws ConfigException;
  
}
