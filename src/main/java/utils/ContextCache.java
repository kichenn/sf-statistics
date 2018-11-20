package utils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import log.LoggerFactory;
import net.jodah.expiringmap.ExpiringMap;
import utils.ContextConstants;

public enum ContextCache {
    INSTANCE();

    private static Map<String, Object> map =
        ExpiringMap.builder().expiration(ContextConstants.EXPIRE_TIME, TimeUnit.SECONDS).build();

    public <T> void putValue(String robotId, String userId, String tag, T value) {
        if (!isValidTag(tag) || value == null)
            return;
        map.put(generateKey(robotId, userId, tag), value);
    }

    public <T> T getValue(String robotId, String userId, String tag, Class<T> type) {
        Object obj = map.get(generateKey(robotId, userId, tag));
        T value = null;

        try {
            value = type.cast(obj);
        } catch (Exception e) {
            LoggerFactory.getLogger().info("ContextCache::getValue exception:" + e.toString());
        }
        return value;
    }

    private boolean isValidTag(String tag) {
        for (String t : ContextConstants.SUPPORTED_TAGS) {
            if (t == tag)
                return true;
        }
        return false;
    }

    private String generateKey(String robotId, String userId, String tag) {
        return tag + robotId + userId;
    }
}
