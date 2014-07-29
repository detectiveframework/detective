package detective.core.common.groovy;

import static org.junit.Assert.*

import org.junit.Test

import detective.core.dsl.table.Row
import detective.core.dsl.table.TableParser;

public class GroovyMixinTest {

  public static class MyVersionOfLeftShift {
    private boolean leftShiftCalled = false;
    public Object leftShift(Object obj){
      leftShiftCalled = true;
      return obj;
    }
    
    def talk(text) {
        "Aargh, walk the plank. ${text}"
    }
  }
  
  @Test
  public void test() {
    String.mixin MyVersionOfLeftShift
  
    assert 'Aargh, walk the plank. Give me a bottle of rum.' == new MyVersionOfLeftShift().talk("Give me a bottle of rum.")
    
    assert "def" == "abc" << "def";
  }
  
}
