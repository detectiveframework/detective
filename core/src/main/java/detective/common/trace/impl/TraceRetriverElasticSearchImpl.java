package detective.common.trace.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import detective.common.DateUtil;
import detective.common.trace.TraceRecord;
import detective.common.trace.TraceRetriver;
import detective.core.services.ElasticSearchClientFactory;

public class TraceRetriverElasticSearchImpl implements TraceRetriver {
  
  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  private final String TRACE_INDEX = "trace";
  
  //TODO Add timezone
  private final String TimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  public List<TraceRecord> queryTraces(String hashKey) {
    List<TraceRecord> records = new ArrayList<TraceRecord>();
    
    return records;
  }

}
