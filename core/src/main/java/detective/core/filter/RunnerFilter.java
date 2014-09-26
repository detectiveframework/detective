package detective.core.filter;

public interface RunnerFilter<T> {

  void doFilter(T t, RunnerFilterChain<T> chain);
  
}
