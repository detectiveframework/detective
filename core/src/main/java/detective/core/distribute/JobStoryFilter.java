package detective.core.distribute;

import detective.core.filter.RunnerFilter;
import detective.core.filter.RunnerFilterChain;

public class JobStoryFilter implements RunnerFilter<JobStoryRunContext>{

  @Override
  public void doFilter(JobStoryRunContext t, RunnerFilterChain<JobStoryRunContext> chain) {
    if (! t.getScriptClassName().equals(t.getJob().getStoryClassName()))
      return;
    
    if (t.getJob().getStoryIndex() != -1 && t.getJob().getStoryIndex() != t.getCurrentStoryIndex())
      return;
      
    System.out.println("Let's run " + t.getJob());
    
    chain.doFilter(t);
  }

}
