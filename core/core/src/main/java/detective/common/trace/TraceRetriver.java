package detective.common.trace;

import java.util.List;

public interface TraceRetriver {

  List<TraceRecord> queryTraces(String hashKey);
  
}
