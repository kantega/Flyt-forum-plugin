package no.kantega.embed;

import java.net.URL;
import java.util.List;

public interface Embedder {
    String getEmbeddedContent(List<URL> links);
}
