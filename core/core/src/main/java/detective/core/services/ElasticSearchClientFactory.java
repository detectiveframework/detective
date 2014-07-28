package detective.core.services;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ElasticSearchClientFactory {

  private ElasticSearchClientFactory() {

  }

  private static Logger logger = LoggerFactory.getLogger(ElasticSearchClientFactory.class);

  private static TransportClient client = null;

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

  public static TransportClient getTransportClient() {
    if (client == null) {
      synchronized (mutex) {
        try {
          Settings settings = ImmutableSettings.settingsBuilder()
              .put("client.transport.sniff", true)
              .put("threadpool.bulk.type", "fixed")
              .put("threadpool.bulk.size", 100)
              .put("threadpool.bulk.queue_size", 1000)
              .put("client.transport.ignore_cluster_name", true)
              // .put("threadpool.bulk", "type", new
              // String[]{"type", "size", "queue_size"}, new
              // String[]{"fixed", "100", "1000"})
              .build();

          settings.getGroups("threadpool").get("bulk").get("type");

          client = new TransportClient(settings)
              .addTransportAddress(new InetSocketTransportAddress(
                  hostName, port));
        } catch (RuntimeException e) {
          logger.error(e.getMessage(), e);
          throw e;
        }
      }

    }

    return client;
  }

}