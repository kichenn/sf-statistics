package utils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;

import config.ConfigManagedService;
import config.Constants;
import log.LoggerFactory;
import type.immutable.ChatRecord;
import type.immutable.Session;

public class LogstashHelper {
    private final Gson GSON = new Gson();
    private final static String LOGSTASH_HOST =  ConfigManagedService.getConfig().getStr(Constants.LOGSTASH_HOST_URL);
    private final static int LOGSTASH_RECORDS_PORT =  ConfigManagedService.getConfig().getInteger(Constants.LOGSTASH_RECORDS_PORT);
    private final static int LOGSTASH_SESSIONS_PORT =  ConfigManagedService.getConfig().getInteger(Constants.LOGSTASH_SESSIONS_PORT);

    private static class LazyHolder {
        public static final LogstashHelper INSTANCE = new LogstashHelper();
    }

    public static LogstashHelper getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void logSentence(ChatRecord record) {
        try {
            // Error check
            if (isBlank(record.getUniqueId()) || isBlank(record.getUserId()) || isBlank(record.getUserQ())) {
                return;
            }
            
            byte[] json = GSON.toJson(record).getBytes("UTF8");
         
            Socket socket = new Socket(LOGSTASH_HOST, LOGSTASH_RECORDS_PORT);
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            out.write(json);
            out.flush();
            out.close();
            socket.close();
        } catch (UnknownHostException e) {
            LoggerFactory.getLogger().error("Unknown Logstash host: " + LOGSTASH_HOST + ":" + LOGSTASH_RECORDS_PORT);
            LoggerFactory.getLogger().error(e);
        } catch (IOException e) {
            LoggerFactory.getLogger().error("Connect to Logstash I/O exception.");
            LoggerFactory.getLogger().error(e);
        }
    }

    public void logSession(Session session) {
        try {
            // Error check
            if (isBlank(session.getSessionId())) {
                return;
            }

            byte[] json = GSON.toJson(session).getBytes("UTF8");

            Socket socket = new Socket(LOGSTASH_HOST, LOGSTASH_SESSIONS_PORT);
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            out.write(json);
            out.flush();
            out.close();
            socket.close();
        } catch (UnknownHostException e) {
            LoggerFactory.getLogger().error("Unknown Logstash host: " + LOGSTASH_HOST + ":" + LOGSTASH_SESSIONS_PORT);
            LoggerFactory.getLogger().error(e);
        } catch (IOException e) {
            LoggerFactory.getLogger().error("Connect to Logstash I/O exception.");
            LoggerFactory.getLogger().error(e);
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.length() <= 0;
    }
}
