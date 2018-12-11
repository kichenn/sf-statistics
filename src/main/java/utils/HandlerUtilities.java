package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlerUtilities {
    private static Pattern invalidIdPattern = Pattern.compile("[^a-zA-Z0-9: _-]", Pattern.CASE_INSENSITIVE);
   private static String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";


    public static boolean isValidParameter(String param) {
        if (param == null || param.isEmpty())
            return false;
        Matcher m = invalidIdPattern.matcher(param);
        return !m.find();
    }

    public static boolean isValidSentence(String param) {
        if (param == null || param.isEmpty())
            return false;
        return param.matches(regex);

    }

    public static boolean isValidEvaluateParameters(Integer isSolved, Integer type, Integer reason) {
        if (type == null) {
            return false;
        } else if (type.equals(0) && isSolved != null) {
            if (isSolved.equals(0) || isSolved.equals(1))
                return true;
        } else if (type.equals(1) && reason != null) {
            if (reason >= 1 && reason <= 9)
                return true;
        } else {
            return false;
        }
        return false;
    }
}
