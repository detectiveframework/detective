package detective.core.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class DetectiveConfig {

  private static final Config config = ConfigFactory.load("detectivve-config.conf");
 
  public static Config getConfig(){
    
//    assertThat(config.hasPath("tasks"), is(true));
//    Config configLogin = config.getConfig("tasks.login");
//    assertThat(configLogin, notNullValue());
//    assertThat(configLogin.getString("url"), CoreMatchers.equalTo("localhost:8080/login_check"));
    return config;
  }
}
