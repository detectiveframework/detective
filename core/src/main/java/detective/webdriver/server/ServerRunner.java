package detective.webdriver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerRunner {
  
  public static void main(String args[]) {
    try {
        Process p = Runtime.getRuntime().exec("java -jar /Users/bglcorp/git/detective/core/src/main/resources/seleniumserver/selenium-server-standalone-2.42.2.jar -timeout=20 -browserTimeout=60");
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line=reader.readLine();

        while (line != null) {    
            System.out.println(line);
            line = reader.readLine();
        }

    }
    catch(IOException e1) {}
    catch(InterruptedException e2) {}

    System.out.println("finished.");
}

}
