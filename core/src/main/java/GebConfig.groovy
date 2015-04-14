import org.openqa.selenium.firefox.FirefoxDriver
import detective.core.Detective;
import geb.report.PageSourceReporter
import geb.report.ReportState
import geb.report.Reporter
import geb.report.ReportingListener
import geb.report.ScreenshotReporter
import detective.core.geb.GebSession

driver = Detective.config.getString("browser.default_driver");

cacheDriverPerThread = true;

waiting {
  timeout = 10
  retryInterval = 0.5
}

reporter = new geb.report.CompositeReporter(new PageSourceReporter(), new ScreenshotReporter());

reportsDir = Detective.config.getString("browser.reportsDir");

reportingListener = new ReportingListener() {
  void onReport(Reporter reporter, ReportState reportState, List<File> reportFiles) {
    reportFiles.each {
      String msg = "[[Browser Screenshot and HTML has been saved into |$it.absolutePath]]" 
      if (GebSession.isParametersAvailable())
        Detective.logUserMessage(GebSession.getParameters(), msg);
        
      println msg
    }
  }
}