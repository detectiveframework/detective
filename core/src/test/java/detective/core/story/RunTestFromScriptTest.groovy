package detective.core.story;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static detective.core.Detective.*;

public class RunTestFromScriptTest {
  
  
  
  @Before
  public void setUp() throws Exception {
    
  }

  @After
  public void tearDown() throws Exception {
    
  }
  
  @Test
  public void testEchoTask(){
    story() "Simple Story with Echo Task" {
      inOrderTo "demostrate simple story"
      
      scenario "Echo parameters back" {
        given "a parameter" {
          parameterA = "This is the value"
        }
        
        when "run echo task"{
          runtask echoTask();
        }
        
        then "parameters will echo back"{
          echotask.parameterA << "This is the value"
        }
      }
    }
  }

  @Test
  public void testHelloWorld() {
    HelloWorldStory story = new HelloWorldStory();
    story.run();
  }
  
  @Test
  public void testRunTestFromScript() {
    StockStory story = new StockStory();
    story.run();
  }
  
  @Test
  public void testSimpleStory() {
    SimpleStory story = new SimpleStory();
    story.run();
  }
  
  @Test
  public void testSimpleCompactStory() {
    SimpleCompactStory story = new SimpleCompactStory();
    story.run();
  }
  
  @Test
  public void testTaskDemoStory() {
    TaskDemoStory story = new TaskDemoStory();
    story.run();
  }
  
  @Test
  public void testTableDemoStory(){
    TableDemoStory story = new TableDemoStory();
    story.run();
  }
}
