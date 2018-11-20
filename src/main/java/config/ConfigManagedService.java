package config;

/**
 * Created by yuao on 11/9/17.
 */
public enum ConfigManagedService {
    INSTANCE();

    private ConfigManager configManager = null;

    ConfigManagedService() {
        configManager = FileConfigManager.getInstance();
    }

    public static ConfigManager getConfig() {
        return ConfigManagedService.INSTANCE.configManager;
    }
}
