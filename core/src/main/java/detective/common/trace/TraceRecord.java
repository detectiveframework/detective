package detective.common.trace;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TraceRecord {
  
  private Date timestamp;
  
  /**
   * What's the event type (Category)? For example Security, ShoppingCar, Activity
   */
  private String type;
  
  /**
   * A event grouped by hashkey, for example you want trace user activity, the type is "Activity" and hashkey is "theusername"
   */
  private String hashKey;
  
  private String caption;
  
  private Long accountId;
  
  private String fundId;
  
  /**
   * Who involved into this trace record, if it's user then put user name in
   */
  private String operator; 
  
  /**
   * What's the thread currently it's running on
   */
  private String threadName;
  
  /**
   * What's current cpu?
   */
  private int cpu;
  
  /**
   * What's the queue involved
   */
  private String queue;
  
  /**
   * Got your own data? pass in here and this can be still searching able, json data start with Object {}
   */
  private String extendJsonData;
  
  /**
   * Extended data for this class.
   * <br>We support only two types: String and Number, any classes extends from java.lang.Number will trade as number otherwise String
   */
  private final Map<String, Object> extendDatas = new HashMap<String, Object>();
  
  

  @Override
  public String toString() {
    return "TraceRecord [timestamp=" + timestamp + ", type=" + type + ", hashKey=" + hashKey
        + ", caption=" + caption + ", accountId=" + accountId + ", fundId=" + fundId
        + ", operator=" + operator + ", threadName=" + threadName + ", cpu=" + cpu + ", queue="
        + queue + ", extendDatas=" + extendDatas + ", extendJsonData=" + extendJsonData + "]";
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public TraceRecord setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public String getType() {
    return type;
  }

  public TraceRecord setType(String type) {
    this.type = type;
    return this;
  }

  public String getHashKey() {
    return hashKey;
  }

  public TraceRecord setHashKey(String hashKey) {
    this.hashKey = hashKey;
    return this;
  }

  public String getOperator() {
    return operator;
  }

  public TraceRecord setOperator(String operator) {
    this.operator = operator;
    return this;
  }

  public String getThreadName() {
    return threadName;
  }

  public TraceRecord setThreadName(String threadName) {
    this.threadName = threadName;
    return this;
  }

  public int getCpu() {
    return cpu;
  }

  public TraceRecord setCpu(int cpu) {
    this.cpu = cpu;
    return this;
  }

  public String getQueue() {
    return queue;
  }

  public TraceRecord setQueue(String queue) {
    this.queue = queue;
    return this;
  }

  /**
   * Got your own data? pass in here and this can be still searching able, json data start with Object {}
   */
  public String getExtendJsonData() {
    return extendJsonData;
  }

  /**
   * Got your own data? pass in here and this can be still searching able, json data start with Object {}
   */
  public TraceRecord setExtendJsonData(String extendJsonData) {
    this.extendJsonData = extendJsonData;
    return this;
  }

  public String getCaption() {
    return caption;
  }

  public TraceRecord setCaption(String caption) {
    this.caption = caption;
    return this;
  }

  public Long getAccountId() {
    return accountId;
  }

  public TraceRecord setAccountId(Long accountId) {
    this.accountId = accountId;
    return this;
  }

  public String getFundId() {
    return fundId;
  }

  public TraceRecord setFundId(String fundId) {
    this.fundId = fundId;
    return this;
  }

  public Map<String, Object> getExtendDatas() {
    return extendDatas;
  }
  
  
}
