package detective.core.distribute.resultrender;

import java.util.List;

import detective.core.distribute.JobRunResult;

public interface ResultRender {

  void render(List<JobRunResult> results, long timeElapsedSec);
  
}
