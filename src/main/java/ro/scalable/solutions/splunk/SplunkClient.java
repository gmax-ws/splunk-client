package ro.scalable.solutions.splunk;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static ro.scalable.solutions.splunk.SplunkConfigKeys.*;

/**
 * Splunk client.
 */
public class SplunkClient {
    private static final Logger log = LoggerFactory.getLogger(SplunkClient.class);

    private final SplunkConfig config;
    private final HttpClient client = HttpClient.newHttpClient();
    private final String uri;

    /**
     * Constructor.
     *
     * @param config Splunk configurations
     */
    private SplunkClient(SplunkConfig config) {
        this.config = config;
        this.uri = String.format("%s/services/collector/event",
                config.getString(SPLUNK_SERVER, "https://localhost"));
    }

    /**
     * Splunk client factory.
     *
     * @param config Splunk configurations
     */
    public static SplunkClient create(SplunkConfig config) {
        return new SplunkClient(config);
    }

    /**
     * Publish an event on Splunk server
     *
     * @param event Event details
     * @return server response
     * @throws JsonProcessingException on json parser errors.
     */
    public String sendEvent(Map<String, Object> event) throws JsonProcessingException {
        String index = config.getString(SPLUNK_INDEX);
        return sendEvent(index, event);
    }

    /**
     * Publish an event on Splunk server
     *
     * @param index Splunk index
     * @param event Event details
     * @return server response
     * @throws JsonProcessingException on json parser errors
     */
    public String sendEvent(String index, Map<String, Object> event) throws JsonProcessingException {
        String json = makeEvent(index, event);
        log.debug("Send event: {} to index: {}", json, index);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .timeout(Duration.ofSeconds(config.getLong(SPLUNK_TIMEOUT, 30L)))
                .header("Authorization", "Splunk " + config.getString(SPLUNK_TOKEN))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
    }

    /**
     * Build and serialize event as a Json string.
     *
     * @param index Splunk index
     * @param event  Event details
     * @return Splunk data to send as a Json string
     * @throws JsonProcessingException on json parser errors
     */
    private String makeEvent(String index, Map<String, Object> event) throws JsonProcessingException {
        SplunkEvent splunkEvent = new SplunkEvent();
        splunkEvent.time = Instant.now().getEpochSecond();
        splunkEvent.host = config.getString(SPLUNK_EVENT_HOST, "localhost");
        splunkEvent.source = config.getString(SPLUNK_EVENT_SOURCE, "source");
        splunkEvent.sourcetype = config.getString(SPLUNK_EVENT_SOURCE_TYPE, "sourcetype");
        splunkEvent.index = index;
        splunkEvent.event = event;

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(splunkEvent);
    }

    /**
     * Splunk data
     */
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static class SplunkEvent {
        long time;
        String host;
        String source;
        String sourcetype;
        String index;
        Map<String, Object> event;
    }
}
