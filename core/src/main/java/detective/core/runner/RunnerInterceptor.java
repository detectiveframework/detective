package detective.core.runner;

import detective.core.Story;

public interface RunnerInterceptor {

  /**
   * invoke before run a story
   * @param story
   * @return false if you don't want the process continue
   */
  boolean beforeRun(Story story);
  
  /**
   * invoke a story finished
   * 
   * @param story
   */
  void afterRun(Story story);
  
}
