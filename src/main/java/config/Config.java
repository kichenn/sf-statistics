package config;

import java.util.HashMap;

/**
 * Created by hyman on 2018/1/16.
 */
public class Config extends HashMap<String, Object> {
    private String configVersion = "";

    public String getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(String configVersion) {
        this.configVersion = configVersion;
    }

}
