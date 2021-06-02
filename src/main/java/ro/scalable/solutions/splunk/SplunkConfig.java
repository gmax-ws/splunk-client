package ro.scalable.solutions.splunk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SplunkConfig extends Properties {
    private static final Logger log = LoggerFactory.getLogger(SplunkConfig.class);

    private SplunkConfig(InputStream inp) throws IOException {
        this.load(inp);
    }

    public String getString(String key) {
        return this.getProperty(key);
    }

    public String getString(String key, String defaultValue) {
        return this.getProperty(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        String value = this.getProperty(key);
        return value == null ? defaultValue : Long.parseLong(value);
    }

    public long getInt(String key, int defaultValue) {
        String value = this.getProperty(key);
        return value == null ? defaultValue : Integer.parseInt(value);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = this.getProperty(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }

    public static SplunkConfig fromResource(String resourceName) {
        try (InputStream rs = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName)) {
            return new SplunkConfig(rs);
        } catch (IOException | NullPointerException e) {
            log.error("Resource {} load error!", resourceName, e);
            throw new RuntimeException(e);
        }
    }

    public static SplunkConfig fromFile(String fileName) {
        try (InputStream fs = new FileInputStream(fileName)) {
            return new SplunkConfig(fs);
        } catch (IOException e) {
            log.error("File {} load error!", fileName, e);
            throw new RuntimeException(e);
        }
    }
}
