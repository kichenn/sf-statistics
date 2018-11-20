package utils;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtils {
    private static Pattern WELCOME_REG_4_RECOMMEND = Pattern.compile("link submit=\"(.*?)\".*?/link");
    private static Gson gson = new Gson();

    public static ArrayList<String> extractString4Link(String target) {
        ArrayList<String> ret = new ArrayList<>();
        if (StringUtils.isBlank(target)) {
            return ret;
        }

        Matcher matcher = WELCOME_REG_4_RECOMMEND.matcher(target);
        while (matcher.find()) {
            ret.add(matcher.group(1));
        }
        return ret;
    }

    public static String reverse(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        return new StringBuilder(s).reverse().toString();
    }

    public static String md5(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            String md5Str = base64en.encode(md5.digest(s.getBytes("utf-8")));
            return md5Str;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String randomInt(int r){
        Random random = new Random();
        int t =  random.nextInt(r);
        return String.valueOf(t);
    }

    public static String printObjectJson(Object o){
        return gson.toJson(o);
    }
}
