package detective.core.common.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import detective.common.utils.ConsoleTable;

public class ConsoleTableTest {

  @Test
  public void test() {
    ConsoleTable t = new ConsoleTable(4, "Title abc def");
    t.appendRow();
    t.appendColum("Id").appendColum("Name").appendColum("Sex").appendColum("Age");

    t.appendRow();
    t.appendColum("1").appendColum("Kong.Denny").appendColum("Male").appendColum("28");

    t.appendRow();
    t.appendColum("2").appendColum("James.luo");
    
    String output = t.toString();
    System.out.println(output);
    
    assertEquals(output, "" +
        "|=========== Title abc def ============|\n" +
        "|  Id  |  Name        |  Sex   |  Age  |\n" +
        "|======================================|\n" +
        "|  1   |  Kong.Denny  |  Male  |  28   |\n" + 
        "|--------------------------------------|\n" + 
        "|  2   |  James.luo   |        |       |\n" +
        "|--------------------------------------|\n");
  }

}
