package detective.common.httpclient;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;

public class IdleConnectionMonitorThread extends Thread {

  private final HttpClientConnectionManager connMgr;
  private volatile boolean shutdown;

  public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
    super();
    this.connMgr = connMgr;
    
    this.setDaemon(true);
  }

  @Override
  public void run() {
    try {
      while (!shutdown) {
        synchronized (this) {
          wait(5000);
          // Close expired connections
          connMgr.closeExpiredConnections();
          // Optionally, close connections
          // that have been idle longer than 30 sec
          connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
        }
      }
    } catch (InterruptedException ex) {
      // terminate
    }
  }

  public void shutdown() {
    synchronized (this) {
      setShutdown(true);
      notifyAll();
    }
  }

  public boolean isShutdown() {
    return shutdown;
  }

  public void setShutdown(boolean shutdown) {
    this.shutdown = shutdown;
  }

}