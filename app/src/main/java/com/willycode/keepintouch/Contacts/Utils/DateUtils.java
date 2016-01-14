package com.willycode.keepintouch.Contacts.Utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Manuel ELO'O on 11/01/2016.
 */
public class DateUtils {
    public static String getDateString(Date d){
        // Create an instance of SimpleDateFormat used for formatting
// the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

// Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
        return df.format(d);
    }

    public static Date getDate(String d){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        try {
            Date date = df.parse(d);
            return date;
        } catch (ParseException e) {
            Log.e("DateUtils",e.getMessage(),e);
           return null;
        }
    }
}
