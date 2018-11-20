package config;

/**
 * Created by yuao on 4/9/17.
 */
public interface ConfigManager {
    /**
     * get positive integer configuration
     * @param keyName
     * @return
     */
    public int getInteger(final String keyName);

    /**
     * get boolean configuration
     * @param keyName
     * @return
     */
    public boolean getBoolean(final String keyName);

    /**
     * get string configuration
     * @param keyName
     * @return
     */
    public String getStr(final String keyName);

    /**
     * get input bytes from configuration file.
     * The key name points to file path.
     * @param keyName
     * @return
     */
    public byte[] getBytes(final String keyName);

    public boolean isCompleted();

}
