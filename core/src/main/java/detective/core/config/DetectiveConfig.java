package detective.core.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public enum DetectiveConfig {
  
  INSTANCE;
  
  private DetectiveConfig(){
    config = configUser.withFallback(configDefault);
  }

  private final Config configDefault = ConfigFactory.load("detective-config-default.conf");
  private final Config configUser = ConfigFactory.load("detective.conf");
  private final Config config;
 
  public static Config getConfig(){
    
//    assertThat(config.hasPath("tasks"), is(true));
//    Config configLogin = config.getConfig("tasks.login");
//    assertThat(configLogin, notNullValue());
//    assertThat(configLogin.getString("url"), CoreMatchers.equalTo("localhost:8080/login_check"));
    return INSTANCE.config;
  }
}
