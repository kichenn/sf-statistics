package config;

import com.google.common.primitives.Bytes;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import log.LoggerFactory;

import java.io.File;

/**
 * Created by yuao on 4/9/17.
 */
public class FileConfigManager implements ConfigManager {

    private final Config rawConfig;

    private final static String configFileName = "Controller.properties";

    private final static FileConfigManager INSTANCE = new FileConfigManager();

    private FileConfigManager() {
        try {
            String path = System.getProperty("user.dir") + "/config/" + configFileName;
            LoggerFactory.getLogger().debug("Config path: " + path);
            rawConfig = ConfigFactory.parseFile(new File(path));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Couldn't read the property file '%s' .", configFileName));
        }
    }

    public static synchronized FileConfigManager getInstance() {
        return INSTANCE;
    }

    public boolean isCompleted() {
        return !INSTANCE.rawConfig.isEmpty();
    }

    /**
     * get positive integer configuration
     *
     * @param keyName
     * @return
     */
    public int getInteger(final String keyName) {
        return rawConfig.getInt(keyName);
    }

    /**
     * get boolean configuration
     *
     * @param keyName
     * @return
     */
    public boolean getBoolean(final String keyName) {
        return rawConfig.getBoolean(keyName);
    }

    /**
     * get string configuration
     *
     * @param keyName
     * @return
     */
    public String getStr(final String keyName) {
        return rawConfig.getString(keyName);
    }

    /**
     * get input bytes from configuration file.
     * The key name points to file path.
     *
     * @param keyName
     * @return
     */
    public byte[] getBytes(final String keyName) {
        return Bytes.toArray(rawConfig.getBytesList(keyName));
    }
}
