package ro.scalable.solutions.splunk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Splunk configuration, a specialization of Properties able to read and convert
 * properties from different sources.
 */
public class SplunkConfig extends Properties {
    private static final Logger log = LoggerFactory.getLogger(SplunkConfig.class);

    /**
     * Constructor
     *
     * @param inp Source input stream
     * @throws IOException throws when something goes wrong.
     */
    private SplunkConfig(InputStream inp) throws IOException {
        this.load(inp);
    }

    /**
     * Get a property as a String value.
     *
     * @param key Configuration key
     * @return Property as a String value or <code>null</code> if property not found.
     */
    public String getString(String key) {
        return this.getProperty(key);
    }

    /**
     * Get a property as a String value.
     * Returns a default value if property is not found.
     *
     * @param key          Configuration key
     * @param defaultValue Default value
     * @return Property as a String value or <code>defaultValue</code> if property not found.
     */
    public String getString(String key, String defaultValue) {
        return this.getProperty(key, defaultValue);
    }

    /**
     * Get a property as a long value.
     * Returns a default value if property is not found.
     *
     * @param key          Configuration key
     * @param defaultValue Default value
     * @return Property as a long or <code>defaultValue</code> if property not found.
     */
    public long getLong(String key, long defaultValue) {
        String value = this.getProperty(key);
        return value == null ? defaultValue : Long.parseLong(value);
    }

    /**
     * Get a property as an integer value.
     * Returns a default value if property is not found.
     *
     * @param key          Configuration key
     * @param defaultValue Default value
     * @return Property as an integer or <code>defaultValue</code> if property not found.
     */
    public long getInt(String key, int defaultValue) {
        String value = this.getProperty(key);
        return value == null ? defaultValue : Integer.parseInt(value);
    }

    /**
     * Get a property as a boolean value.
     * Returns a default value if property is not found.
     *
     * @param key          Configuration key
     * @param defaultValue Default value
     * @return Property as a boolean value  or <code>defaultValue</code> if property not found.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = this.getProperty(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }

    /**
     * Build a SplunkConfig instance from a resource.
     *
     * @param resourceName Resource name
     * @return an instance of SplunkConfig or raise exception in case of an error.
     */
    public static SplunkConfig fromResource(String resourceName) {
        try (InputStream rs = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName)) {
            return new SplunkConfig(rs);
        } catch (IOException | NullPointerException e) {
            log.error("Resource {} load error!", resourceName, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Build a SplunkConfig instance from a file.
     *
     * @param fileName File name
     * @return an instance of SplunkConfig or raise exception in case of an error.
     */
    public static SplunkConfig fromFile(String fileName) {
        try (InputStream fs = new FileInputStream(fileName)) {
            return new SplunkConfig(fs);
        } catch (IOException e) {
            log.error("File {} load error!", fileName, e);
            throw new RuntimeException(e);
        }
    }
}
