package no.kantega.embed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.singletonMap;

public class DefaultEmbedder implements Embedder {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getEmbeddedContent(List<URL> links) {
        List<Map<String, String>> content = links.stream()
                .map(l -> singletonMap("html", "<a target=\"_blank\" href=\"" + l.toString() + "\">" + l.toString() + "</a>"))
                .collect(Collectors.toList());
        try {
            return objectMapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            log.error("Error converting " + content, e);
            return null;
        }
    }
}
