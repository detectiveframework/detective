package detective.core;

import detective.core.testdsl.stock.SweaterStockManagerTask;
import detective.core.testdsl.superstream.centralhub.GetAbnStatusTask;
import detective.task.EchoTask;

public class TestTaskFactory {
  

  /**
   * Sweater Stock Manager for testing. take below inputs:
   * <ul>
   *   <li>sweater.black * : number</li>
   *   <li>sweater.blue * : number</li>
   *   <li>sweater.refund.blue : number</li>
   *   <li>sweater.refund.black : number</li>
   *   <li>sweater.replace.blue : number</li>
   *   <li>sweater.replace.black : number</li>
   * </ul>
   * 
   * outputs
   * <ul>
   *  <li>sweater.black : number : how many black sweaters left on stock</li>
   *  <li>sweater.blue : number : how many blue sweaters left on stock</li>
   * </ul>
   *
   */
  public static SweaterStockManagerTask stockManagerTask(){
    return new SweaterStockManagerTask();
  }
  
  public static SweaterStockManagerTask stockManagerTask1(){
    return new SweaterStockManagerTask();
  }
  
  public static EchoTask echo(){
    return new EchoTask();
  }
  
  public static GetAbnStatusTask getAbnStatusTask(){
  	return new GetAbnStatusTask();
  }

}
