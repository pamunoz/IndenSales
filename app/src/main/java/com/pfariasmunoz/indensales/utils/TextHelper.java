package com.pfariasmunoz.indensales.utils;

import android.text.TextUtils;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Pablo Farias on 21-04-17.
 */

public class TextHelper {

    public static String capitalizeFirestLetter(String text) {
        StringBuilder sb = new StringBuilder();

        String[] textArray = text.split(" ");
        for (String word : textArray) {
            String newword = upperCaseFirst(word) + " ";
            sb.append(newword);
        }
        return sb.toString().trim();

    }

    private static String upperCaseFirst(String value) {
        String word = "";
        if (value != null && !TextUtils.isEmpty(value)) {

            word = value.toLowerCase();
            if (word.length() > 2)
                return word.substring(0, 1).toUpperCase() + word.substring(1);
            else {
                return word.substring(0, 1).toUpperCase();
            }

        }
        return word;
    }


    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    public static String formatDate(String stringTimeInMillis) {
        long timeInMillis = Long.valueOf(stringTimeInMillis);
        Date dateObject = new Date(timeInMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    public static String formatTime(String stringTimeInMillis) {
        long timeInMillis = Long.valueOf(stringTimeInMillis);
        Date dateObject = new Date(timeInMillis);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

}
