package detective.webdriver.browser;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.grid.common.GridRole;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.utils.SelfRegisteringRemote;

public class Node {
  

  public static void main(String[] args) throws Exception{
    RegistrationRequest req = new RegistrationRequest(); 
    req.setRole(GridRole.NODE);

    Map<String, Object> nodeConfiguration = new HashMap<String, 
    Object>();

    nodeConfiguration.put(RegistrationRequest.AUTO_REGISTER, true); 
    nodeConfiguration.put(RegistrationRequest.HUB_HOST, "localhost");

    nodeConfiguration.put(RegistrationRequest.HUB_PORT, 4444); 
    nodeConfiguration.put(RegistrationRequest.PORT, 5555);

    URL remoteURL = new URL("http://" + "<your local ip address>" + ":" + 5555); 
    nodeConfiguration.put(RegistrationRequest.PROXY_CLASS, "org.openqa.grid.selenium.proxy.DefaultRemoteProxy"); 
    nodeConfiguration.put(RegistrationRequest.MAX_SESSION, 1); 
    nodeConfiguration.put(RegistrationRequest.CLEAN_UP_CYCLE, 2000); 
    nodeConfiguration.put(RegistrationRequest.REMOTE_HOST, remoteURL); 
    nodeConfiguration.put(RegistrationRequest.MAX_INSTANCES, 1);

    req.setConfiguration(nodeConfiguration);

    SelfRegisteringRemote remote = new SelfRegisteringRemote(req); 
    remote.startRemoteServer(); 
    remote.startRegistrationProcess(); 
  }

}
