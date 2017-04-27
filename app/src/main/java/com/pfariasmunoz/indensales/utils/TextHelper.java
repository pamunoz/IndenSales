package com.pfariasmunoz.indensales.utils;

/**
 * Created by Pablo Farias on 21-04-17.
 */

public class TextHelper {

    public static String capitalizeFirestLetter(String text) {
        String[] words = text.split(" ");
        StringBuilder sb = new StringBuilder();

        if (words.length > 0) {

            for (int i = 0; i < words.length; i++) {
                if (words[i].contains(".")) {
                    sb.append(words[i]);
                    sb.append(" ");
                } else {
                    String lastword = words[i].substring(0, 1).toUpperCase() + words[i].substring(1);
                    if (i < words.length) {
                        sb.append(lastword);
                        sb.append(" ");
                    } else {
                        sb.append(lastword);
                    }
                }
            }
        }
        return sb.toString();

    }

}
