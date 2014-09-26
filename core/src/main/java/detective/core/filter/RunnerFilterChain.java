package detective.core.filter;


public interface RunnerFilterChain<T> extends Iterable<RunnerFilter<T>> {
  
  void doFilter(T t);
  
  void resetChainPosition();

}
