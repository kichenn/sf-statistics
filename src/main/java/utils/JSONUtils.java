package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Helper for Convert to JSON String.
 */
public class JSONUtils {
    private static final Gson convertor = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    static public String toJSon(Object o) {
        return convertor.toJson(o);
    }

    static public <T> T fromJSON(String data, Class<T> classOfT) {
        return convertor.fromJson(data, classOfT);
    }

    private JSONUtils() {}
}
