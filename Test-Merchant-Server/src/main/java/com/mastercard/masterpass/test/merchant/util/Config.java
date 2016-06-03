package com.mastercard.masterpass.test.merchant.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Helper for reading config.properties file
 */
public class Config {

    /**
     * Filename with properties. Should be in resource and in .war in WEB-INF/classes
     */
    public static final String PROPERTIES_FILE = "config.properties";

    /**
     * Key to read which profile is currently active
     */
    public static final String KEY_ACTIVE_PROFILE = "profile.active";

    /**
     * Reference to read properties
     */
    public static Properties properties;

    /**
     * Currently active profile
     */
    public static String activeProfile;

    /**
     * Read properties files. Read once and then cache in memory
     *
     * @return properties object
     */
    public Properties readProperties() {
        // Check if we have already read properties
        if (properties != null) {
            return properties;
        }
        // In case of first read, create object
        properties = new Properties();
        // Open filestream
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        if (inputStream != null) {
            try {
                // Convert stream to properties
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Tidy up
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        // Read current profile
        activeProfile = properties.getProperty(KEY_ACTIVE_PROFILE);

        return properties;
    }
}
