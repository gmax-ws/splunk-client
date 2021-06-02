package ro.scalable.solutions;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.scalable.solutions.splunk.SplunkClient;
import ro.scalable.solutions.splunk.SplunkConfig;

import java.time.Instant;
import java.util.Map;

public class TestClient {
    private static final Logger log = LoggerFactory.getLogger(TestClient.class);

    public static void main(String[] args) throws JsonProcessingException {
        String configFile = "splunk.config";
        SplunkConfig config = SplunkConfig.fromResource(configFile);

        SplunkClient splunk = SplunkClient.create(config);
        Map<String, Object> event = Map.of(
                "name", "Hello Splunk!",
                "type", "METRICS",
                "timestamp", Instant.now());
        String result = splunk.sendEvent(event);

        log.info(result);
    }
}
