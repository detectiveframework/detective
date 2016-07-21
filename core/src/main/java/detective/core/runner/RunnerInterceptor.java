package detective.core.runner;

import detective.core.Story;

public interface RunnerInterceptor {

  /**
   * invoke before run a story
   * @return false if you don't want the process continue
   */
  boolean beforeRun(Story story);
  
  /**
   * invoke a story finished
   *
   */
  void afterRun(Story story);
  
}
