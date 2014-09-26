package detective.common.trace.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
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
    
    TransportClient client = ElasticSearchClientFactory.getTransportClient();
    SearchRequestBuilder builder = client.prepareSearch(TRACE_INDEX);
    TermQueryBuilder query = new TermQueryBuilder("hashKey", hashKey);
    builder.setQuery(query);
    builder.setSize(1000);
    builder.addSort("timestamp", SortOrder.DESC);
    
    ListenableActionFuture<SearchResponse> future = builder.execute();
    SearchResponse res = future.actionGet();

    SearchHits hits = res.getHits();
    for (SearchHit hit : hits){
      Map<String, Object> sources = hit.getSource();
      Set<String> keys = new HashSet<String>();
      keys.addAll(sources.keySet());
      
      TraceRecord record = new TraceRecord();
      try {
        record.setTimestamp(DateUtil.parse(TimeFormat, sources.get("timestamp").toString()));
      } catch (ParseException e) {        
        logger.error(e.getMessage(), e);
        throw new RuntimeException(e.getMessage(), e);
      }
      record.setType(sources.get("type").toString());
      record.setHashKey(sources.get("hashKey").toString());
      
      record.setAccountId(Long.valueOf(sources.get("accountId").toString()));
      record.setFundId(sources.get("fundId").toString());
      record.setCaption(sources.get("caption").toString());
      record.setOperator(sources.get("operator").toString());
      record.setThreadName(sources.get("threadName").toString());
      record.setQueue(sources.get("queue").toString());
      record.setCpu(Integer.valueOf(sources.get("cpu").toString()));
      record.setExtendJsonData(sources.get("extendJsonDataOriginal").toString());
      
      keys.remove("timestamp");
      keys.remove("type");
      keys.remove("hashKey");
      keys.remove("accountId");
      keys.remove("fundId");
      keys.remove("caption");
      keys.remove("operator");
      keys.remove("threadName");
      keys.remove("queue");
      keys.remove("cpu");
      keys.remove("extendJsonDataOriginal");
      keys.remove("extendJsonData");
      
      for (String key : keys){
        record.getExtendDatas().put(key, sources.get(key));
      }
      
      records.add(record);
    }
    
    return records;
  }

}
