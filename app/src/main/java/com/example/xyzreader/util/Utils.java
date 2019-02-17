package com.example.xyzreader.util;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    private static SimpleDateFormat outputFormat = new SimpleDateFormat();
    private static GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2, 1, 1);

    public static String getFormattedDate(String date) {
        Date dateToFormat = parseDate(date);
        if (!dateToFormat.before(START_OF_EPOCH.getTime())) {
            return DateUtils.getRelativeTimeSpanString(dateToFormat.getTime(), System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
        } else {
            return outputFormat.format(dateToFormat);
        }
    }

    private static Date parseDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
            Log.i(TAG, "passing today's date");
            return new Date();
        }
    }
}
