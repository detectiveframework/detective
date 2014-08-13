package detective.core.dsl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

import detective.core.Parameters;
import detective.core.exception.ImmutableElementException;
import detective.core.services.DetectiveFactory;

/**
 * Create a tree like parameters. 
 * always try to get/set parent parameter first.
 * 
 * @author bglcorp
 *
 */
public class ParametersImpl implements Parameters{
  
  private static final Logger logger = LoggerFactory.getLogger(ParametersImpl.class);
  
  private Parameters parent;
  
  public Parameters getParent() {
    return parent;
  }
  
  public void setParent(Parameters parent) {
    this.parent = parent;
  }

  private boolean immutable = false;
  
  public ParametersImpl(){
    this(null);
  }
  
  public ParametersImpl(Parameters parent){
    map = new ConcurrentHashMap<String, Object>();
    this.parent = parent;
  }
  
  public static ParametersImpl createFromMap(Map<String, Object> map){
    ParametersImpl result = new ParametersImpl();
    for (String key : map.keySet()){
      result.put(key, map.get(key));
    }
    return result;
  }

  private final Map<String, Object> map;
  
  
  
  @Override
  public String toString() {
    return "Parameters [parent=" + parent + ", current =" + map + "]";
  }

  @Override
  public Object get(String key) {
    if (parent != null && parent.containsKey(key)){
      return parent.get(key);
    }
      
    Object obj = map.get(key);
    if (obj != null && obj instanceof WrappedObject){
      return getWrappedValue((WrappedObject<?>)obj);
    }
    
    if (obj == null)
      obj = DetectiveFactory.INSTANCE.getParametersConfig().getUnwrappered(key);
    
    return obj;
  }
  
  private Object getWrappedValue(WrappedObject<?> obj){
    Object unwrapped = obj.getValue();
    
    if (unwrapped instanceof WrappedObject)
      return getWrappedValue((WrappedObject<?>)unwrapped);
    else
      return unwrapped;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public Object put(String key, Object value) {
    if (immutable)
      throw new ImmutableElementException("Parameter is immutable, you may try to setup a parameter which comes from global settings. Key:" + key);
    
    if (parent != null && parent.containsKey(key)){
      return parent.put(key, value);
    }
    
    Object oldValue = map.get(key);
    if (oldValue != null && oldValue instanceof SharedVariable){
      if (this.isUnbind(oldValue)){
        ((SharedVariable)oldValue).setValue(value);
        return oldValue;
      }else{
        //throw new DslException("Shared data [" + key + "] can only setup once.");
        logger.info("As shared data [" + key + "] can only setup once, your value ignored.");
      }
    }
    
    return map.put(key, value);
  }

  @Override
  public Set<String> keySet() {
    if (parent == null)
      return map.keySet();
    else{
      Set<String> keys = new HashSet<String>();
      keys.addAll(parent.keySet());
      keys.addAll(map.keySet());
      return keys;
    }
  }

  @Override
  public int size() {
    if (parent == null)
      return map.size();
    else
      return parent.size() + map.size();
  }

  @Override
  public boolean isEmpty() {
    if (parent == null)
      return map.isEmpty();
    else
      return parent.isEmpty() && map.isEmpty();
  }

  @Override
  public boolean containsKey(String key) {
    if (parent == null)
      return map.containsKey(key);
    else
      return parent.containsKey(key) || map.containsKey(key);
  }

  @Override
  public Parameters clone() {
    Parameters p = new ParametersImpl();
    p.putAllUnwrappered(this);
    p.setImmutable(this.isImmutable());
    return p;
  }

  @Override
  public void putAll(Parameters parameters) {
    for (String key : parameters.keySet()){
       this.put(key, parameters.get(key));
    }
  }
  
  @Override
  public void putAllUnwrappered(Parameters parameters) {
    if (immutable)
      throw new ImmutableElementException("Parameter is immutable, you may try to setup a parameter which comes from global settings.");
    
    for (String key : parameters.keySet()){
       map.put(key, parameters.getUnwrappered(key));
    }
  }

  @Override
  public Object remove(String key) {
    if (immutable)
      throw new ImmutableElementException("Parameter is immutable, you may try to setup a parameter which comes from global settings. Key:" + key);
    
    if (parent == null)
      return map.remove(key);
    else{
      if (parent.containsKey(key))
        return parent.remove(key);
      else
        return map.remove(key);
    }
  }

  @Override
  public Object getUnwrappered(String key) {
    Object obj = getUnwrapperedInner(key);
    if (obj == null){
      if (DetectiveFactory.INSTANCE.getParametersConfig().containsKey(key))
        obj = DetectiveFactory.INSTANCE.getParametersConfig().getUnwrappered(key);
    }
      
    return obj;
  }

  private Object getUnwrapperedInner(String key) {
    if (parent == null)
      return map.get(key);
    else{
      if (parent.containsKey(key))
        return parent.getUnwrappered(key);
      else
        return map.get(key);
    }
  }

  @Override
  public Set<String> getUnbindShareVarKeys() {
    Set<String> result = new HashSet<String>();
    for (String key : this.keySet()){
      Object obj = this.getUnwrappered(key);
      if (isUnbind(obj))
        result.add(key);
    }
    return result;
  }
  
  private boolean isUnbind(Object value){
    return value instanceof SharedVariable && !((SharedVariable<?>)value).isBound();
  }

  @Override
  public boolean isInParent(String key) {
    if (parent == null)
      return false;
    
    if (map.containsKey(key))
      return false;
    
    if (parent.containsKey(key))
      return true;
    else
      return false;
  }

  public boolean isImmutable() {
    return immutable;
  }

  public void setImmutable(boolean immutable) {
    this.immutable = immutable;
  }

}
