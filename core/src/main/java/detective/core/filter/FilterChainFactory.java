package detective.core.filter;

import detective.core.services.DetectiveFactory;

public interface FilterChainFactory {

  RunnerFilterChain<?> getChain();
  
  public static class ConfigReader{
    
    public static FilterChainFactory instanceFromConfigFile(String configName) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      String factoryClass = DetectiveFactory.INSTANCE.getConfig().getString(configName);
      if (factoryClass != null){
        Class<?> clazz = Class.forName(factoryClass);
        Object instance = clazz.newInstance();
        return FilterChainFactory.class.cast(instance);
      }else
        throw new RuntimeException(configName + " not found in config file.");
    }
  }
}
