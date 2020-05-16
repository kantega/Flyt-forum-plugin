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

    private final URL apiUrl;
    private final String apiKey;
    private final boolean forceSecure;
    private final boolean forceHttps;
    private final String apiUrlEncoding;

    @Inject
    public Embedly(@Named("embed.ly.api.url") URL apiUrl,
                   @Named("embed.ly.api.key") String apiKey,
                   @Named("embed.ly.api.url.encoding") String apiUrlEncoding,
                   @Named("embed.ly.api.url.forceSecure") boolean forceSecure,
                   @Named("embed.ly.api.url.forceHttps") boolean forceHttps) {
        this.apiUrlEncoding = apiUrlEncoding;
        this.apiUrl = requireNonNull(apiUrl, "May not be null: apiUrl");
        this.apiKey = requireNonNull(apiKey, "May not be null: apiKey");
        this.forceSecure = forceSecure;
        this.forceHttps = forceHttps;
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
        Oembed.OembedBuilder builder = oembed()
            .withUrls(links)
            .withNostyle(true);
        if(forceHttps) {
            builder.withSecure(true);
        }
        if(forceHttps) {
            builder.withScheme(Oembed.Scheme.https);
        }
        Oembed oembed = builder
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
