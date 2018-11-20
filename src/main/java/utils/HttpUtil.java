package utils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import log.LoggerFactory;

/**
 * Created by duoduo on 17/3/30.
 */
public class HttpUtil {



    public static String getBodyStr(HttpServletRequest request) throws IOException {

        InputStream inputStream= request.getInputStream();
        byte[] bytes = read(inputStream);
        String result = new String(bytes, "utf-8");
        return result;
    }


    public static byte[] read(InputStream inStream) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outStream.toByteArray();
    }
    
    public static String get(String urlStr, int timeout) {
        LoggerFactory.getLogger().info(String.format("Http get: %s", urlStr));
        HttpURLConnection conn = null;
        String result = "";

        try{
            URL url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept-charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(timeout);

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK){
                throw new RuntimeException("Failed: HTTP error code: " + responseCode);
            }
            else{
                InputStream inputStream = conn.getInputStream();
                byte[] data = read(inputStream);
                result = new String(data, "UTF-8");
            }
        }catch (Exception e){
            e.printStackTrace();
            LoggerFactory.getLogger().info(e.getMessage());
        }finally {
            if (conn != null)
                conn.disconnect();
        }

        return result;
    }

    public static String post(String urlStr, String json, int timeout) {
        LoggerFactory.getLogger().info(String.format("Http post: %s; json: %s", urlStr, json));

        HttpURLConnection conn = null;
        String result = "";

        try{
            URL url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
            // 设置超时时间
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(timeout);

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.flush();
            os.close();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
            }
            else{
                InputStream inputStream = conn.getInputStream();
                byte[] data = read(inputStream);
                result = new String(data, "UTF-8");
            }
        }catch (Exception e){
            e.printStackTrace();
            LoggerFactory.getLogger().info(e.getMessage());
        }finally {
            if (conn != null)
                conn.disconnect();
        }

        return result;
    }
}
