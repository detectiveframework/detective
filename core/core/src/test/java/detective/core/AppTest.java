package detective.core;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
  /**
   * Create the test case
   * 
   * @param testName name of the test case
   */
  public AppTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(AppTest.class);
  }

  /**
   * Rigourous Test :-)
   */
  public void testApp() {
    assertTrue(true);
  }

  // Whenever you write a library, allow people to supply a Config but
  // also default to ConfigFactory.load if they don't supply one.
  // Libraries generally have some kind of Context or other object
  // where it's convenient to place the configuration.

  public static class SimpleLibContext {
    private Config config;

    // we have a constructor allowing the app to provide a custom Config
    public SimpleLibContext(Config config) {
      this.config = config;
      // This verifies that the Config is sane and has our
      // reference config. Importantly, we specify the "simple-lib"
      // path so we only validate settings that belong to this
      // library. Otherwise, we might throw mistaken errors about
      // settings we know nothing about.
      config.checkValid(ConfigFactory.defaultReference(), "simple-lib");
    }

    // This uses the standard default Config, if none is provided,
    // which simplifies apps willing to use the defaults
    public SimpleLibContext() {
      this(ConfigFactory.load());
    }

    // this is the amazing functionality provided by simple-lib
    public void printSetting(String path) {
      System.out.println("The setting '" + path + "' is: " + config.getString(path));
    }
  }

  // Simple-lib is a library in this same examples/ directory.
  // This method demos usage of that library with the config
  // provided.
  private static void demoConfigInSimpleLib(Config config) {
    SimpleLibContext context = new SimpleLibContext(config);
    context.printSetting("simple-lib.foo");
    context.printSetting("simple-lib.hello");
    context.printSetting("simple-lib.whatever");
  }

  public void testLoadConfigFile() {
    // This app is "complex" because we load multiple separate app
    // configs into a single JVM and we have a separately-configurable
    // context for simple lib.

    // system property overrides work, but the properties must be set
    // before the config lib is used (config lib will not notice changes
    // once it loads the properties)
    System.setProperty("simple-lib.whatever", "This value comes from a system property");
    System.setProperty("simple-lib.hello", "This value comes from a system property for hello");

    // "config1" is just an example of using a file other than
    // application.conf
    Config config1 = ConfigFactory.load("detective/core/AppTest.conf");

    // use the config ourselves
    System.out.println("config1, complex-app.something="
        + config1.getString("complex-app.something"));

    // use the config for a library
    demoConfigInSimpleLib(config1);

    // ////////

    // "config2" shows how to configure a library with a custom settings
    // subtree
    Config config2 = ConfigFactory.load("detective/core/AppTest1.conf");

    // use the config ourselves
    System.out.println("config2, complex-app.something="
        + config2.getString("complex-app.something"));
    
    System.out.println(config2.getString("complex-app.simple-lib-context.simple-lib.foo").toString());

    // pull out complex-app.simple-lib-context and move it to
    // the toplevel, creating a new config suitable for our
    // SimpleLibContext.
    // The defaultOverrides() have to be put back on top of the stack so
    // they still override any simple-lib settings.
    // We fall back to config2 again to be sure we get simple-lib's
    // reference.conf plus any other settings we've set. You could
    // also just fall back to ConfigFactory.referenceConfig() if
    // you don't want complex2.conf settings outside of
    // complex-app.simple-lib-context to be used.
    Config simpleLibConfig2 =
        ConfigFactory.defaultOverrides().withFallback(
            config2.getConfig("complex-app.simple-lib-context")).withFallback(config2);

    demoConfigInSimpleLib(simpleLibConfig2);


    // Here's an illustration that simple-lib will get upset if we pass it
    // a bad config. In this case, we'll fail to merge the reference
    // config in to complex-app.simple-lib-context, so simple-lib will
    // point out that some settings are missing.
    try {
      demoConfigInSimpleLib(config2.getConfig("complex-app.simple-lib-context"));
    } catch (ConfigException.ValidationFailed e) {
      System.out.println("when we passed a bad config to simple-lib, it said: " + e.getMessage());
    }

  }
}
