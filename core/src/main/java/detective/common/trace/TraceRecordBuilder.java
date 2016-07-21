package detective.common.trace;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import detective.common.DateUtil;
import detective.common.json.JacksonMsgConverter;
import detective.core.services.DetectiveFactory;

public class TraceRecordBuilder {

  private static final Logger logger = LoggerFactory.getLogger(TraceRecordBuilder.class);

  private static final JacksonMsgConverter msgConverter = new JacksonMsgConverter();

  private final TraceRecord record;
  
  private static String instanceId;

  private TraceRecordBuilder(TraceRecord record) {
    this.record = record;
  }
  
  /**
   * Setup the time stamp to now and create a new TraceRecordBuilder and Thread to current thread
   * name
   * 
   * @return the new record
   */
  public static TraceRecordBuilder newRecord() {
    TraceRecord record = new TraceRecord();
    return build(record);
  }

  /**
   * Setup the time stamp to now and create a new TraceRecordBuilder and Thread to current thread
   * name
   */
  public static TraceRecordBuilder build(TraceRecord record) {
    if (instanceId == null)
      instanceId = DetectiveFactory.INSTANCE.getMachineName();
    
    record.setTimestamp(new Date()).setThreadName(Thread.currentThread().getName());
    return new TraceRecordBuilder(record).withObject("_RunningInstance", instanceId);
  }

  public static TraceRecordBuilder buildUnhandledException(Throwable exception, String type, String hashKey) {
    logger.error(exception.getMessage(), exception);
    
    TraceRecord record = new TraceRecord();
    record.setType(type);
    record.setHashKey(hashKey);
    record.getExtendDatas().put("_Exception", "UnhandledException");
    return build(record).withException(exception);
  }

  public TraceRecord getRecord() {
    return record;
  }
  
  /**
   * Setup HashKey as yyyy-MM-dd as of now
   * @return the builder
   */
  public TraceRecordBuilder withSimpleDateAsHashKey(){
    record.setHashKey(DateUtil.formatSimpleDate(new Date()));
    return this;
  }

  /**
   * Always add below information into TraceRecord
   * <ul>
   * <li>request.path</li>
   * <li>request.parameters</li>
   * <li>request.cookies</li>
   * <li>request.content</li>
   * </ul>
   *
   */
  public TraceRecordBuilder withHttpRequest(HttpServletRequest request) {
    Map<String, Object> map = record.getExtendDatas();
    map.put("request.path", request.getRequestURL().toString());
    Map<String, String[]> paramMap = request.getParameterMap();
    if (paramMap != null && paramMap.size() > 0) {
      map.put("request.parameters", this.msgConverter.toJson(request.getParameterMap()));
    }

    if (request.getCookies() != null && request.getCookies().length > 0){
      StringBuilder sb = new StringBuilder("[");
      for (Cookie c : request.getCookies()) {
        sb.append("{\"").append(c.getName()).append("\":\"").append(c.getValue()).append("\"},");
      }
      sb.append("]");
      map.put("request.cookies", sb.toString());
    }
    
    map.put("request.content", getRequestInputStream(request));

    return this;
  }

  /**
   * Always add below exception information into TraceRecord
   * <ul>
   * <li>exception.msg</li>
   * <li>exception.callstack</li>
   * <li>exception.class</li>
   * </ul>
   */
  public TraceRecordBuilder withException(Throwable exception) {
    Map<String, Object> map = record.getExtendDatas();
    if (null != exception.getMessage())
      map.put("exception.msg", exception.getMessage());
    map.put("exception.class", exception.getClass().getName());
    map.put("exception.callstack", getStackTrace(exception));

    return this;
  }

  /**
   * Add extend data into trace record, this basically doing: record.getExtendDatas().put(fieldName,
   * toJson(obj));
   *
   * @param obj, will convert to json before put into extend data, will use toString() if convert to
   *          json fails
   */
  public TraceRecordBuilder withObject(String fieldName, Object obj) {
    String json = toJsonSafe(obj);
    record.getExtendDatas().put(fieldName, json);
    return this;
  }

  /**
   * Add extend data into trace record, this basically doing: record.getExtendDatas().put(fieldName,
   * obj);
   */
  public TraceRecordBuilder withObject(String fieldName, Number obj) {
    record.getExtendDatas().put(fieldName, obj);
    return this;
  }

  /**
   * Add extend data into trace record, this basically doing: record.getExtendDatas().put(fieldName,
   * obj);
   */
  public TraceRecordBuilder withObject(String fieldName, String obj) {
    record.getExtendDatas().put(fieldName, obj);
    return this;
  }
  
  
  public TraceRecordBuilder withPayLoad(Object obj){
    record.getExtendDatas().put("payload", toJsonSafe(obj));
    return this;
  }

  private static String toJsonSafe(Object obj) {
    try {
      if (obj == null)
        return null;
      
      if (obj instanceof String)
        return (String) obj;

      String result = msgConverter.toJson(obj);
      return result;
    } catch (Throwable e) {
      return "Convert to JSON error:" + e.getMessage() + " the object:" + obj.toString();
    }
  }

  private static String getRequestInputStream(HttpServletRequest request) {
    try {
      return IOUtils.toString(request.getInputStream(), "UTF-8");
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
      return "ERROR:" + e.getMessage();
    }
  }

  private static String getStackTrace(Throwable aThrowable) {
    Writer result = new StringWriter();
    PrintWriter printWriter = new PrintWriter(result);
    aThrowable.printStackTrace(printWriter);
    return result.toString();
  }
}
