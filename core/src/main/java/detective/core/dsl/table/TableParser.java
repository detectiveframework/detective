package detective.core.dsl.table;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.runtime.GroovyCategorySupport;

import detective.core.Parameters;
import detective.core.dsl.builder.ShareDataAwardDelegate;
import detective.core.runner.PropertyToStringDelegate;

public class TableParser {

  static ThreadLocal<List<Row>> context = new ThreadLocal<List<Row>>();

  public static Row or(Object self, Object arg) {
    return appendRow(self, arg);
  }

  public static Row or(Integer self, Integer arg) {
    return appendRow(self, arg);
  }

  public static Row or(Boolean self, Boolean arg) {
    return appendRow(self, arg);
  }

  public static Row appendRow(Object value, Object nextValue) {
    Row row = new Row(value);
    context.get().add(row);
    return row.or(nextValue);
  }

  public static List<Row> asListOfRows(Parameters existsParameters, Closure<?> tableData) {
    context.set(new ArrayList<Row>());
    tableData.setDelegate(new ShareDataAwardDelegate(existsParameters));
    tableData.setResolveStrategy(Closure.DELEGATE_FIRST);

    GroovyCategorySupport.use(TableParser.class, tableData);
    return context.get();
  }

}
