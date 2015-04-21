package detective.core.matcher;

import detective.utils.TablePrinter;

public class SubsetAssertError extends AssertionError {

  private static final long serialVersionUID = 1L;
  
  /**
   * We don't want serial all objects
   */
  private String fullTableStrVersion;
  private String subsetTableStrVersion;
  
  public SubsetAssertError(Object fullTable, Object subsetTable, String msg, Throwable cause){
    super(msg, cause);
    
    fullTableStrVersion = TablePrinter.printObjectAsTable(fullTable, "Full / Origin Table");
    subsetTableStrVersion = TablePrinter.printObjectAsTable(subsetTable, "Subset / Expected Table");    
  }

  public String getFullTableStrVersion() {
    return fullTableStrVersion;
  }

  public void setFullTableStrVersion(String fullTableStrVersion) {
    this.fullTableStrVersion = fullTableStrVersion;
  }

  public String getSubsetTableStrVersion() {
    return subsetTableStrVersion;
  }

  public void setSubsetTableStrVersion(String subsetTableStrVersion) {
    this.subsetTableStrVersion = subsetTableStrVersion;
  }

}
