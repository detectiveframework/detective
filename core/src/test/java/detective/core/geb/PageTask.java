package detective.core.geb;

import geb.Browser;
import geb.Page;
import detective.core.Detective;
import detective.core.Parameters;
import detective.task.AbstractTask;

public class PageTask extends AbstractTask {
  
  private final ThreadLocal<Browser> browser;

  private final Page page;
  
  public PageTask(Page page){
    browser = new ThreadLocal<Browser>();
    browser.set(Detective.newBrowser());
    this.page = page;
  }
  
  public PageTask(Class<? extends Page> pageClass) throws InstantiationException, IllegalAccessException{
    this(pageClass.newInstance());
  }
  
  @Override
  protected void doExecute(Parameters config, Parameters output) {
    browser.get().to(page.getClass());
    browser.get().getPage().getPageUrl();
    output.put("browser", browser.get());
  }

}
