package detective.core.story;

import static org.junit.Assert.*;

import detective.core.story.matcher.ErrorExpectStory
import detective.core.story.matcher.SubsetJsonTableStory
import detective.core.story.matcher.SubsetListClassTableStory
import detective.core.story.matcher.SubsetListMapTableStory
import detective.core.story.matcher.TableSubsetMatcherWithClosureStory
import detective.core.story.matcher.TableSubsetMatcherWithRowIndexMoreCaseStory
import detective.core.story.matcher.TableSubsetMatcherWithRowIndexStory
import detective.core.story.table.TableMoreCaseStory
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
  public void testTaskMoreCaseStory() {
    TableMoreCaseStory story = new TableMoreCaseStory();
    story.run();
  }
  
  
  @Test
  public void testTableDemoStory(){
    TableDemoStory story = new TableDemoStory();
    story.run();
  }
  
  @Test
  public void testErrorExpectStory(){
    ErrorExpectStory story = new ErrorExpectStory();
    story.run();
  }
  
  @Test
  public void testTableSubsetMatcherWithClosureStory(){
    TableSubsetMatcherWithClosureStory story = new TableSubsetMatcherWithClosureStory();
    story.run();
  }
  
  @Test
  public void testTableSubsetMatcherWithRowIndexMoreCaseStory(){
    TableSubsetMatcherWithRowIndexMoreCaseStory story = new TableSubsetMatcherWithRowIndexMoreCaseStory();
    story.run();
  }
  
  @Test
  public void testTableSubsetMatcherWithRowIndexStory(){
    TableSubsetMatcherWithRowIndexStory story = new TableSubsetMatcherWithRowIndexStory();
    story.run();
  }
  
  @Test
  public void testSubsetJsonTableStory(){
    SubsetJsonTableStory story = new SubsetJsonTableStory();
    story.run();
  }
  
  @Test
  public void testSubsetListClassTableStory(){
    SubsetListClassTableStory story = new SubsetListClassTableStory();
    story.run();
  }
  
  @Test
  public void testSubsetListMapTableStory(){
    SubsetListMapTableStory story = new SubsetListMapTableStory();
    story.run();
  }
}
