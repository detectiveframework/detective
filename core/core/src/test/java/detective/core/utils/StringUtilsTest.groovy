package detective.core.utils;

import static org.junit.Assert.*

import org.junit.Assert
import org.junit.Test

import detective.utils.StringUtils

public class StringUtilsTest {

  @Test
  public void testEditDistance() {
    Assert.assertTrue(StringUtils.editDistance("sweater.black", "sweater.black") == 0);
    Assert.assertTrue(StringUtils.editDistance("sweater.balck", "sweater.black") == 2);
    Assert.assertTrue(StringUtils.editDistance("sweater.balkc", "sweater.black") == 3);
  }
  
  @Test
  public void testBestMatch() {
    assert ! StringUtils.getBestMatch("abc", []).isPresent();
    assert StringUtils.getBestMatch("abc", [""]).isPresent();
    assert "sweater.black" == StringUtils.getBestMatch("sweater.balck", ["sweater.black", "sweater.blue", "sweater.refund.black"]).get();
  }
  

}
