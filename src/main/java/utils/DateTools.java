package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTools {

    public enum DateFormat {
        DATE_FORMAT_stand("yyyy-MM-dd HH:mm:ss"),
        DATE_FORMAT_request_day("yyyy-MM-dd");

        private DateFormat(String format) {
            this.format = format;
        }

        private String format;

    }

    public static Date str2Date(String datestr, DateFormat format, boolean beginOrEnd) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format.format);
        Date date = sdf.parse(datestr);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        if (beginOrEnd) {

            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        } else {

            calendar.set(Calendar.HOUR, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
        }
        return calendar.getTime();

    }

    public static String contructDaySpanStr(Date begin, Date end) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT_request_day.format);
        StringBuilder sb = new StringBuilder();
        sb.append(sdf.format(begin));
        sb.append("-");
        sb.append(sdf.format(end));
        return sb.toString();
    }

    public static long gapDayOfTwo(Date first, Date second) {
        long gapDay = (first.getTime() - second.getTime()) / (1000 * 3600 * 24);
        if (gapDay < 0L)
            gapDay = Math.abs(gapDay);
        return gapDay;
    }
}
