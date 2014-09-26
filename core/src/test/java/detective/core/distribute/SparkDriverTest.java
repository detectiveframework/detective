package detective.core.distribute;

import static org.junit.Assert.*;

import org.junit.Test;

public class SparkDriverTest {

  @Test
  public void test() {
    SparkDriver.run("detective.core.distribute.collect");
  }

}
