package detective.common.json;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.type.TypeReference;


public class JacksonMsgConverter extends ObjectMapper {

  private static final String FULLTIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

  public JacksonMsgConverter() {
    this(true);
  }

  public JacksonMsgConverter(boolean failOnUnknowProperties) {
    configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
        failOnUnknowProperties);
    configure(Feature.WRITE_DATES_AS_TIMESTAMPS, false);
    SimpleDateFormat format = new SimpleDateFormat(FULLTIME_FORMAT);
    format.setTimeZone(TimeZone.getTimeZone("UTC"));
    setDateFormat(format);
  }

  public String toJson(Object obj) {
    try {
      return writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T fromJson(String json, Class<T> valueType) {
    try {
      return this.readValue(json, valueType);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Map<String, Object> fromJsonToMap(String json) {
    try {
      return this.readValue(json, new TypeReference<HashMap<String, Object>>() {
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}