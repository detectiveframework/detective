package detective.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
  // Financial years are always start from 1st of July.
  public static final int FINANCIAL_YEAR_START_MONTH = 7;

  public static final SimpleDateFormat SIMPLE_DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  public static final SimpleDateFormat SIMPLE_DAY_TIME_FORMAT = new SimpleDateFormat(
      "yyyy-MM-dd HH:mm:ss");

  public static final SimpleDateFormat REPORT_DATE_FORMAT = new SimpleDateFormat("dd MMMMM yyyy");

  /**
   * TODO Wrong Name
   */
  public static String fromatFullTime(Date date) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    format.setTimeZone(TimeZone.getTimeZone("UTC")); // Coordinated
    // Universal Time OR
    // Greenwich Mean
    // Time
    return format.format(date);
  }

  public static String formatFullTime(Date date) {
    return formatFullTime(date, TimeZone.getTimeZone("UTC"));
  }

  public static String formatFullTime(Date date, TimeZone timeZone) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    if (timeZone != null) {
      format.setTimeZone(timeZone);
    }

    return format.format(date);
  }

  public static Date parseFullTime(String str) throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    return format.parse(str);
  }

  public static Date parseFullTime(String str, TimeZone timeZone) throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    format.setTimeZone(timeZone);

    return format.parse(str);
  }

  /**
   * Format date with UTC time zone.
   */
  public static String format(Date date, String pattern) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    return sdf.format(date);
  }


  public static Date parse(String pattern, String dateStr) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    return sdf.parse(dateStr);
  }

  public static String formatSimpleDate(Date date) {
    if (date == null) {
      return "";
    }
    return SIMPLE_DAY_FORMAT.format(date);
  }

  public static Date parseSimpleDate(String string, Date defaultDate) {
    if (string == null) {
      return defaultDate;
    }

    try {
      return SIMPLE_DAY_FORMAT.parse(string);
    } catch (ParseException e) {
      return defaultDate;
    }
  }


}
