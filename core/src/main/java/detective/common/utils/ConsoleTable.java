package detective.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Denny.Kong(Kong Fan Zhi)
 * 
 */
public class ConsoleTable {
  private List<List> rows = new ArrayList<List>();

  private int colum;

  private int[] columLen;

  private static int margin = 2;

  private String title = null;

  public ConsoleTable(int colum, String title) {
    this.title = title != null ? " " + title + " " : null;
    this.colum = colum;
    this.columLen = new int[colum];
  }

  public void appendRow() {
    List row = new ArrayList(colum);
    rows.add(row);
  }

  public ConsoleTable appendColum(Object value) {
    if (value == null) {
      value = "null";
    }
    List row = rows.get(rows.size() - 1);
    row.add(value);
    int len = value.toString().getBytes().length;
    if (columLen[row.size() - 1] < len)
      columLen[row.size() - 1] = len;
    return this;
  }

  public String toString() {
    StringBuilder buf = new StringBuilder();

    int sumlen = 0;
    for (int len : columLen) {
      sumlen += len;
    }
    if (title != null) {

      String headerLine = printChar('=', sumlen + margin * 2 * colum + (colum - 1));

      headerLine = headerLine.substring(0, headerLine.length() - title.length());
      int fixLength = 0;
      if (headerLine.length() % 2 != 0) {
        fixLength = 1;
      }
      int halfLength = (int) (headerLine.length() * 0.5);
      headerLine = headerLine.substring(0, (int) (headerLine.length() * 0.5));
      headerLine = headerLine + title + printChar('=', halfLength + fixLength);
      buf.append("|").append(headerLine).append("|\n");
    } else
      buf.append("|").append(printChar('-', sumlen + margin * 2 * colum + (colum - 1))).append(
          "|\n");
    for (int ii = 0; ii < rows.size(); ii++) {
      List row = rows.get(ii);
      for (int i = 0; i < colum; i++) {
        String o = "";
        if (i < row.size())
          o = row.get(i).toString();
        buf.append('|').append(printChar(' ', margin)).append(o);
        buf.append(printChar(' ', columLen[i] - o.getBytes().length + margin));
      }
      buf.append("|\n");
      if (title != null && ii == 0) {

        buf.append("|").append(printChar('=', sumlen + margin * 2 * colum + (colum - 1))).append(
            "|\n");

      } else
        buf.append("|").append(printChar('-', sumlen + margin * 2 * colum + (colum - 1))).append(
            "|\n");
    }
    return buf.toString();
  }

  private String printChar(char c, int len) {
    StringBuilder buf = new StringBuilder();
    for (int i = 0; i < len; i++) {
      buf.append(c);
    }
    return buf.toString();
  }

}
