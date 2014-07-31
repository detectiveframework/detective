package detective.core.testdsl.stock;

import java.util.Map;

import detective.core.Parameters;
import detective.core.TestTask;
import detective.core.config.ConfigException;
import detective.task.AbstractTask;

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
 * @author James Luo
 *
 */
public class SweaterStockManagerTask extends AbstractTask implements TestTask{
  
  @Override
  protected void doExecute(Parameters config, Parameters output) {
    Long blackSweater = this.readAsLong(config, "sweater.black", null, false, "sweater.black : number"); 
    Long blueSweater = this.readAsLong(config, "sweater.blue", null, false, "sweater.blue : number");
    
    Long backRefundSweater = this.readAsLong(config, "sweater.refund.black", null, true, null); 
    Long blueRefundSweater = this.readAsLong(config, "sweater.refund.blue", null, true, null);
    Long backReplaceSweater = this.readAsLong(config, "sweater.Replace.black", null, true, null); 
    Long blueReplaceSweater = this.readAsLong(config, "sweater.Replace.blue", null, true, null);
    
    if (backRefundSweater == null && blueRefundSweater == null && backReplaceSweater == null && blueReplaceSweater == null){
      throw new ConfigException("sweater.refund.black sweater.refund.blue sweater.Replace.black sweater.Replace.blue at least one of them has value");
    }
    
    if (backRefundSweater != null)
      blackSweater = blackSweater + backRefundSweater;
    
    if (blueRefundSweater != null)
      blueSweater = blueSweater + blueRefundSweater;
    
    if (backReplaceSweater != null)
      blackSweater = blackSweater + backReplaceSweater;
    
    if (blueReplaceSweater != null)
      blueSweater = blueSweater + blueReplaceSweater;
    
    output.put("sweater.black", blackSweater);
    output.put("sweater.blue", blueSweater);
  }

}
