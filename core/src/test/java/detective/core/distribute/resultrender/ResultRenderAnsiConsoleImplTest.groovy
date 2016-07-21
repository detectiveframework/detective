package detective.core.distribute.resultrender;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.jansi.Ansi;
import org.junit.Test;

import detective.core.distribute.JobRunResult;
import detective.core.distribute.JobRunResult.JobRunResultSteps
import junit.framework.TestCase;

/**
 * The class <code>ResultRenderAnsiConsoleImplTest</code> contains tests for
 * the class {@link <code>ResultRenderAnsiConsoleImpl</code>}
 *
 * @pattern JUnit Test Case
 *
 * @generatedBy CodePro at 30/03/15 3:01 PM
 *
 * @author james
 *
 * @version $Revision$
 */
public class ResultRenderAnsiConsoleImplTest extends TestCase {

  /**
   * Construct new test instance
   *
   * @param name the test name
   */
  public ResultRenderAnsiConsoleImplTest(String name) {
    super(name);
  }
  
  /**
   * To Run this main method, you can :
   * mvn exec:java -Dexec.mainClass="detective.core.distribute.resultrender.ResultRenderAnsiConsoleImplTest" -Dexec.classpathScope=test
   */
  public static void main(String[] args) {
    ResultRenderAnsiConsoleImplTest test = new ResultRenderAnsiConsoleImplTest("ResultRenderAnsiConsoleImplTest");
    test.testRender();
  }
  
  @Test
  public void testRender() {
    ResultRenderAnsiConsoleImpl render = new ResultRenderAnsiConsoleImpl();
    
    List<JobRunResult> results = createTestCases();
    
    render.render(results, 0);
  }

  private List<JobRunResult> createTestCases() {
    List<JobRunResult> results = new ArrayList<JobRunResult>();
    
    results.add(createResult("Story1", "Scenario1", true, false, null));
    results.add(createResult("Story2", "Scenario2", false, false, null));
    results.add(createResult("Story3", "Scenario3", false, true, null));
    
    results.add(createResult("Story4", "Scenario4", false, false, createNewException("Ansi Console with Exception Message.")));
    return results;
  }
  
  @Test
  public void testSuccessStoryWithUserMessagesInEachStep(){
    checkReuslt(createResult("Story1", "Scenario1", true, false, null),
      """
[32;1mStory Name: Story1[22m
[1m| -- Scenario Name: [22mScenario1
[1m| -- Successed:     [22mYes
[32;1m| -- [22mstep1
[1m| --   message for step1[22m
[1m| --   message2 for step1[22m
[32;32;1m| -- [22mstep2
[1m| --   message for step2[22m
[32;0m
    """
      )
  }
  
  @Test
  public void testFailedStoryWithUserMessagesInEachStep(){
    checkReuslt(createResult("Story2", "Scenario2", false, false, null),
"""
[31;1mStory Name: Story2[22m
[1m| -- Scenario Name: [22mScenario2
[1m| -- Successed:     [22mFailed
[32;1m| -- [22mstep1
[1m| --   message for step1[22m
[1m| --   message2 for step1[22m
[31;31;1m| -- [22mstep2
[1m| --   message for step2[22m
[31;0m
    """
      )
  }
  
  @Test
  public void testIgornedStory(){
    checkReuslt(createResult("Story3", "Scenario3", false, true, null),
      """
[34;1mStory Name: Story3[22m
[1m| -- Scenario Name: [22mScenario3
[1m| -- Ignored:       Yes[22m
[32;1m| -- [22mstep1
[1m| --   message for step1[22m
[1m| --   message2 for step1[22m
[34;31;1m| -- [22mstep2
[1m| --   message for step2[22m
[34;0m
    """);
  }
  
  @Test
  public void testFailedStoryWithCallStack(){
    checkReuslt(createResult("Story4", "Scenario4", false, false, createNewException("Ansi Console with Exception Message.")),  """
[31;1mStory Name: Story4[22m
[1m| -- Scenario Name: [22mScenario4
[1m| -- Successed:     [22mFailed
[32;1m| -- [22mstep1
[1m| --   message for step1[22m
[1m| --   message2 for step1[22m
[31;31;1m| -- [22mstep2
[1m| --   message for step2[22m
[31;1m| -- Error:         Ansi Console with Exception Message.[22m
[1m| -- Error Callstack:[22mjava.lang.RuntimeException: Ansi Console with Exception Message.
  at detective.core.distribute.resultrender.ResultRenderAnsiConsoleImplTest.createNewException(ResultRenderAnsiConsoleImplTest.groovy
  at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
[m
    """)
  }
  
  private void checkReuslt(JobRunResult jobRunResult, String expectedAnsiOutput){
    ResultRenderAnsiConsoleImpl render = new ResultRenderAnsiConsoleImpl();
    String realOutput = render.createAnsiCode(jobRunResult).toString();
    println realOutput;
    expectedAnsiOutput.split("\n").each {
      assert realOutput.contains(it.trim());
    }
  }
  
  private Throwable createNewException(String msg){
    try{
      throw new RuntimeException(msg);
    }catch (Exception e){
      return e;
    }
  }
  
  private JobRunResult createResult(String storyName, String scenarioName, boolean successed, boolean ignored, Throwable error){
    JobRunResult result = new JobRunResult();
    result.setStoryName(storyName);
    result.setScenarioName(scenarioName);
    result.setSuccessed(successed);
    result.setIgnored(ignored);
    result.setError(error);
    
    if (successed){
      List<JobRunResultSteps> steps = new ArrayList<JobRunResultSteps>();
      steps << new JobRunResultSteps(stepName:"step1", successed:true, additionalMsgs:["message for step1", "message2 for step1"]);
      steps << new JobRunResultSteps(stepName:"step2", successed:true, additionalMsgs:["message for step2"]);
      result.setSteps(steps)
    }else{
      List<JobRunResultSteps> steps = new ArrayList<JobRunResultSteps>();
      steps << new JobRunResultSteps(stepName:"step1", successed:true, additionalMsgs:["message for step1", "message2 for step1"]);
      steps << new JobRunResultSteps(stepName:"step2", successed:false, additionalMsgs:["message for step2"]);
      result.setSteps(steps)
    }
    
    
    return result;
  }
  
}

/*$CPS$ This comment was generated by CodePro. Do not edit it.
 * patternId = com.instantiations.assist.eclipse.pattern.testCasePattern
 * strategyId = com.instantiations.assist.eclipse.pattern.testCasePattern.junitTestCase
 * additionalTestNames = 
 * assertTrue = false
 * callTestMethod = true
 * createMain = false
 * createSetUp = false
 * createTearDown = false
 * createTestFixture = false
 * createTestStubs = false
 * methods = 
 * package = detective.core.distribute.resultrender
 * package.sourceFolder = detective.core/src/test/java
 * superclassType = junit.framework.TestCase
 * testCase = ResultRenderAnsiConsoleImplTest
 * testClassType = detective.core.distribute.resultrender.ResultRenderAnsiConsoleImpl
 */