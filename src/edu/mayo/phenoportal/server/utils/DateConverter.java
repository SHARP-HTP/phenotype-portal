package edu.mayo.phenoportal.server.utils;

import java.util.Calendar;
import java.util.Date;

public class DateConverter {

    // create an array of months
    private static String[] STR_MONTHS = new String[] { "January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October", "November", "December" };

    /**
     * Get the date as a formatted string like Day-Month-Year "08-May-2012"
     * 
     * @param date
     * @return
     */
    public static String getDateString(Date date) {
        String dateStr;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        // month is 0 based, so add 1
        dateStr = cal.get(Calendar.DAY_OF_MONTH) + "-" + STR_MONTHS[cal.get(Calendar.MONTH)] + "-"
                + cal.get(Calendar.YEAR);

        return dateStr;
    }

    public static String getTimeString(Date date) {
        String dateStr;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        // month is 0 based, so add 1
        dateStr = cal.get(Calendar.DAY_OF_MONTH) + "-" + STR_MONTHS[cal.get(Calendar.MONTH)] + "-"
                + cal.get(Calendar.YEAR) + "  " + cal.get(Calendar.HOUR) + ":"
                + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.MILLISECOND);

        return dateStr;
    }
}
