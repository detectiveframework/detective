package detective.core.common;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import detective.core.config.DetectiveConfig;


public class ConfigTest extends Assert {
  
  @Test
  public void testSubConfig() {
    Config config = ConfigFactory.load("detective/core/common/unit-test-task-config.conf");
    assertThat(config.hasPath("tasks"), is(true));
    Config configLogin = config.getConfig("tasks.login");
    assertThat(configLogin, notNullValue());
    assertThat(configLogin.getString("url"), CoreMatchers.equalTo("localhost:8080/login_check"));
  }
  
  @Test
  public void testDetectiveConfig(){
    //User config should override default one
    Assert.assertEquals(DetectiveConfig.getConfig().getString("data.folder"), "changed");
    
    Assert.assertEquals(DetectiveConfig.getConfig().getInt("httpclient.max_connections"), 200);
    Assert.assertEquals(DetectiveConfig.getConfig().getString("browser.default_driver"), "firefox");
  }

}
