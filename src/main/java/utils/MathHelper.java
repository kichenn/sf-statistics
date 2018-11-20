package utils;

import java.util.Random;

/**
 * Created by yuao on 25/9/17.
 */
public class MathHelper {
    public static Integer getRandomInteger(int min, int max) {
        if (min == max) {
            return min;
        }
        if (min < 0 || max < 0) {
            return null;
        }
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
