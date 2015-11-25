package no.kantega.embed;

import no.kantega.utilities.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import static no.kantega.utilities.Objects.requireNonNull;


public class Embedly implements Embedder {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String DEFAULT_URL_ENCODING = "UTF-8";

    private URL apiUrl;
    private String apiKey;
    private String apiUrlEncoding;

    @Inject
    public Embedly(@Named("embed.ly.api.url") URL apiUrl, @Named("embed.ly.api.key") String apiKey, @Named("embed.ly.api.url.encoding") String apiUrlEncoding) {
        this.apiUrlEncoding = apiUrlEncoding;
        this.apiUrl = requireNonNull(apiUrl, "May not be null: apiUrl");
        this.apiKey = requireNonNull(apiKey, "May not be null: apiKey");
    }

    public URL getApiUrl() {
        return apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrlEncoding() {
        return apiUrlEncoding;
    }

    public Oembed.OembedBuilder oembed() {
        return Oembed.withEmbedly(this);
    }


    @Override
    public String getEmbeddedContent(List<URL> links) {
        Oembed oembed = oembed()
                .withUrls(links)
                .withNostyle(true)
                .build();
        try {
            try (Http.HttpRequest request = oembed.getHttpRequest();
                 Http.HttpResponse response = request.getResponse()) {
                    if (200 == response.getResponseCode()) {
                        try (InputStream inputStream = response.getInputStream()) {
                            return StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
                        }
                    }
            }
        } catch (Exception cause) {
            log.error("Could not perform embed", cause);
        }

        return null;
    }
}
