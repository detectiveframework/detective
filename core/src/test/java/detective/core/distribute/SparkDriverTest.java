package detective.core.distribute;

import org.junit.Test;

public class SparkDriverTest {

  @Test
  public void test() {
    SparkDriver.main(new String[]{"detective.core.distribute.collect"});
  }

}
