package utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class DebugUtil {
    public static String convertException2String(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
