package com.pfariasmunoz.indensales.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Pablo Farias on 26-04-17.
 */

public class MathHelper {


    public static String setTotalPrice(int amount, String price) {
        BigDecimal decimalAmount = new BigDecimal(amount);
        BigDecimal decimalPrice = new BigDecimal(price);
        BigDecimal totalPrice = decimalAmount.multiply(decimalPrice);
        return totalPrice.setScale(2, RoundingMode.CEILING).toString();
    }

    public static boolean isNumeric(String inputData) {
        return inputData.matches("[-+]?\\d+(\\.\\d+)?");
    }

}
