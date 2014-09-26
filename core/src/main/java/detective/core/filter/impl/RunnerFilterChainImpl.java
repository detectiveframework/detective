package detective.core.filter.impl;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;

import detective.common.annotation.NotThreadSafe;
import detective.core.Scenario;
import detective.core.Story;
import detective.core.TestTask;
import detective.core.filter.RunnerFilter;
import detective.core.filter.RunnerFilterChain;

@NotThreadSafe
public class RunnerFilterChainImpl<T> implements RunnerFilterChain<T>{
  
  private int currentPosition = 0;
  private final List<RunnerFilter<T>> filters;
  
  public RunnerFilterChainImpl(List<RunnerFilter<T>> filters){
    this.filters = ImmutableList.copyOf(filters);
  }

  @Override
  public void doFilter(T t) {
    if (currentPosition >= filters.size())
      return; //we reached the end of filter list
    
    RunnerFilter<T> next = filters.get(currentPosition);
    currentPosition++;
    
    next.doFilter(t, this);
  }
  
  public void resetChainPosition(){
    currentPosition = 0;
  }

  @Override
  public Iterator<RunnerFilter<T>> iterator() {
    return filters.iterator();
  }

}
