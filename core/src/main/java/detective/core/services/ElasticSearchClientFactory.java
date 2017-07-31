package detective.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ElasticSearchClientFactory {

  private ElasticSearchClientFactory() {

  }

  private static Logger logger = LoggerFactory.getLogger(ElasticSearchClientFactory.class);

  private static Object mutex = new Object();

  private static String hostName;
  
  private static int port;

  public static String getHostName() {
    return hostName;
  }

  public static void setHostName(String esName) {
    hostName = esName;
  }
  
  public static void setPort(int esPort){
    port = esPort;
  }
}