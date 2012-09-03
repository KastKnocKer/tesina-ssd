package utility; 

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {
  public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
  public static final String TIME_FORMAT_NOW = "HH:mm:ss";
  
  public static String now_dateTime() {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
    return sdf.format(cal.getTime());
  }
  
  public static String now_time() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT_NOW);
	    return sdf.format(cal.getTime());
  }

}