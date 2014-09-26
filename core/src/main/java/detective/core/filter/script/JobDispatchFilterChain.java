package detective.core.filter.script;

import java.util.List;

import detective.core.Story;
import detective.core.filter.RunnerFilter;
import detective.core.filter.impl.RunnerFilterChainImpl;

public class JobDispatchFilterChain extends RunnerFilterChainImpl<Story> {

  public JobDispatchFilterChain(List<RunnerFilter<Story>> filters) {
    super(filters);
  }

}
