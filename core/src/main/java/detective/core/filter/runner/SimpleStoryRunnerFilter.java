package detective.core.filter.runner;

import detective.core.Story;
import detective.core.filter.RunnerFilter;
import detective.core.filter.RunnerFilterChain;
import detective.core.runner.SimpleStoryRunner;

/**
 * A normal runner that just pass parameter to SimpleStoryRunner
 * 
 * @author James Luo
 * 
 */
public class SimpleStoryRunnerFilter implements RunnerFilter<Story> {

  @Override
  public void doFilter(Story story, RunnerFilterChain<Story> chain) {

    new SimpleStoryRunner(null).run(story, null);

    chain.doFilter(story);
  }

}
