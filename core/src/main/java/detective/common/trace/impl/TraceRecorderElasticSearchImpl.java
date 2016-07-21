package detective.common.trace.impl;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.Date;

import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import detective.common.DateUtil;
import detective.common.trace.TraceRecord;
import detective.common.trace.TraceRecorder;
import detective.core.services.ElasticSearchClientFactory;

public class TraceRecorderElasticSearchImpl implements TraceRecorder {
  
  private final String TRACE_INDEX = "detective-trace";
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  public void record(TraceRecord trace) {
    logger.info(trace.toString());
    return;
    
//    if (trace.getThreadName() == null)
//      trace.setThreadName(Thread.currentThread().getName());
//    
//    TransportClient client = ElasticSearchClientFactory.getTransportClient();
//    IndexRequestBuilder builder = client.prepareIndex(TRACE_INDEX, trace.getType());
//      
//    try {
//      XContentBuilder source = jsonBuilder()
//          .startObject()
//            .field("timestamp", trace.getTimestamp())
//            .field("type", trace.getType())
//            .field("hashKey", trace.getHashKey())
//            .field("caption", trace.getCaption())
//            .field("accountId", trace.getAccountId())
//            .field("fundId", trace.getFundId())
//            .field("operator", trace.getOperator())
//            .field("threadName", trace.getThreadName())
//            .field("cpu", trace.getCpu())
//            .field("queue", trace.getQueue());
//      
//      if (trace.getExtendDatas().size() > 0){
//        for (String key : trace.getExtendDatas().keySet()){
//          Object value = trace.getExtendDatas().get(key);
//          if (value instanceof String || value instanceof Number || null == value){
//            source.field(key, value);
//          }else{
//            throw new RuntimeException("We support only String and Number, if it's a Date please format as String first, the key contains error data type:" + key);
//          }          
//        }
//      }
//      if (trace.getExtendJsonData() != null && trace.getExtendJsonData().length() > 0){
//        source.rawField("extendJsonData", trace.getExtendJsonData().getBytes());
//        source.field("extendJsonDataOriginal", trace.getExtendJsonData());
//      }
//      source.endObject();
//      
//      builder.setSource(source);
//    } catch (IOException e) {
//      throw new RuntimeException(e.getMessage(), e);
//    }
//    
//    ListenableActionFuture<IndexResponse> future = builder.execute();
//    future.actionGet();
  }

  private void buildString(TraceRecord trace) {
    StringBuilder sb = new StringBuilder();
    sb.append("{")
        .append("\"timestamp\":").append("\"").append(DateUtil.formatFullTime(trace.getTimestamp())).append("\",") //.append(trace.getTimestamp().getTime()).append(",")  //DateUtil.formatFullTime(trace.getTimestamp())
        .append("\"type\":\"").append(trace.getType()).append("\",")
        .append("\"hashKey\":\"").append(trace.getHashKey()).append("\",")
        .append("\"caption\":\"").append(trace.getCaption()).append("\",")
        .append("\"accountId\":").append(trace.getAccountId()).append(",")
        .append("\"fundId\":\"").append(trace.getFundId()).append("\",")
        .append("\"operator\":\"").append(trace.getOperator()).append("\",")
        .append("\"threadName\":\"").append(trace.getThreadName()).append("\",")
        .append("\"cpu\":").append(trace.getCpu()).append(",")
        .append("\"queue\":\"").append(trace.getQueue()).append("\"");
    
    if (trace.getExtendDatas().size() > 0){
      for (String key : trace.getExtendDatas().keySet()){
        Object value = trace.getExtendDatas().get(key);
        if (value != null){
          sb.append(",\"").append(key).append("\":");
          if (value instanceof java.lang.Number)
            sb.append(value.toString());
          else if (value instanceof java.util.Date)
            sb.append("\"").append(DateUtil.formatFullTime((Date)value)).append("\"");
          else
            sb.append("\"").append(value.toString()).append("\"");
        }        
      }
    }
    
    if (trace.getExtendJsonData() != null && trace.getExtendJsonData().length() > 0){
      sb.append(",\"extendJsonData\":").append(trace.getExtendJsonData()).append("");
      sb.append(",\"extendJsonDataOriginal\":\"").append(trace.getExtendJsonData()).append("\"");
    }
    
    sb.append("}");
    
    //builder.setSource(sb.toString());
  }
  
  /**
   * The hidden function for test, idealy a log/trace should never been deleted as it is happened, we can't go back
   */
  public void deleteByHashKey(String hashKey){
    if (hashKey == null || hashKey.equals("*"))
      throw new RuntimeException("Hashkey must have a value and can't be *");
    
    TransportClient client = ElasticSearchClientFactory.getTransportClient();
    DeleteByQueryRequestBuilder builder = client.prepareDeleteByQuery(this.TRACE_INDEX);
    TermQueryBuilder query = new TermQueryBuilder("hashKey", hashKey);
    builder.setQuery(query);
    
    ListenableActionFuture<DeleteByQueryResponse> future = builder.execute();
    future.actionGet();
  }

}
