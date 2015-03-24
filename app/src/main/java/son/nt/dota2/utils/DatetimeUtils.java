package son.nt.dota2.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DatetimeUtils {
    // SimpleDateFormat can be used to control the date/time display format:
    //   E (day of week): 3E or fewer (in text xxx), >3E (in full text)
    //   M (month): M (in number), MM (in number with leading zero)
    //              3M: (in text xxx), >3M: (in full text full)
    //   h (hour): h, hh (with leading zero)
    //   m (minute)
    //   s (second)
    //   a (AM/PM)
    //   H (hour in 0 to 23)
    //   z (time zone)

    private static final String MY_DATE_FORMAT = "yyyy-MM-dd (E) HH:mm:ss";

    public static String getTimeFromLongTime(String format, long time) {
        String s = "";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);

            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            s = dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }


    public static String getTimeCurrent() {
        return getTimeFromLongTime(MY_DATE_FORMAT, System.currentTimeMillis());
    }

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    public static String getTimeAgo(long time, Context ctx) {
        // TODO: use DateUtils methods instead
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE) {
            return "just now";
        } else if (diff < 2 * MINUTE) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE) {
            return diff / MINUTE + " minutes ago";
        } else if (diff < 90 * MINUTE) {
            return "an hour ago";
        } else if (diff < 24 * HOUR) {
            return diff / HOUR + " hours ago";
        } else if (diff < 48 * HOUR) {
            return "yesterday";
        } else {
            return diff / DAY + " days ago";
        }
    }


    public static String convertFbTime(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS");
            long when = dateFormat
                    .parse(time)
                    .getTime();
            Date localDate = new Date(when + TimeZone.getDefault().getRawOffset() + (TimeZone.getDefault().inDaylightTime(new Date()) ? TimeZone.getDefault().getDSTSavings() : 0));
            return getTimeFromLongTime(MY_DATE_FORMAT, localDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return time;
        }
    }

}
