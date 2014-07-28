package detective.common;

import java.io.File;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigReader {

  private final Config config;
  
  public ConfigReader(String fileName){
    config = ConfigFactory.parseFile(new File(fileName));
  }

  public Config getConfig() {
    return config;
  }
  
}
