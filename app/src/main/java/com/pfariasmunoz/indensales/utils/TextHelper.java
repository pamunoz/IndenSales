package com.pfariasmunoz.indensales.utils;

import android.text.TextUtils;

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

}
