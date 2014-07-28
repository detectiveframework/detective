package detective.core.config;

@SuppressWarnings("serial")
public class ConfigException extends RuntimeException {

  public ConfigException(String message) {
    super(message);
  }
  
  public static ConfigException configCantEmpty(){
    throw new ConfigException("Config can't empty");
  }

}
