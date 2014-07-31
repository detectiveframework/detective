package detective.core.dsl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.ImmutableMap;

import detective.core.Parameters;

public class ParametersImpl implements Parameters{
  
  public ParametersImpl(){
    map = new ConcurrentHashMap<String, Object>();
  }
  
  public ParametersImpl(Map<String, Object> map){
    this.map = map;
  }

  private final Map<String, Object> map;
  
  @Override
  public Object get(String key) {
    return map.get(key);
  }

  @Override
  public Object put(String key, Object value) {
    return map.put(key, value);
  }

  @Override
  public Set<String> keySet() {
    return map.keySet();
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsKey(String key) {
    return map.containsKey(key);
  }

  @Override
  public Parameters immutable() {
    return new ParametersImpl(ImmutableMap.copyOf(map));
  }

  @Override
  public void putAll(Parameters parameters) {
    map.putAll(((ParametersImpl)parameters).map);
  }

  @Override
  public Object remove(String key) {
    return map.remove(key);
  }

}
