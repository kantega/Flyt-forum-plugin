package no.kantega.embed;

import no.kantega.utilities.Http;
import org.springframework.util.StreamUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import static no.kantega.utilities.Objects.requireNonNull;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-01
 */
public class Embedly {

    public static final String DEFAULT_URL_ENCODING = "UTF-8";

    private URL apiUrl;
    private String apiKey;
    private String urlEncoding = DEFAULT_URL_ENCODING;

    @Inject
    public Embedly(@Named("embed.ly.api.url") URL apiUrl, @Named("embed.ly.api.key") String apiKey) {
        this.apiUrl = requireNonNull(apiUrl, "May not be null: apiUrl");
        this.apiKey = requireNonNull(apiKey, "May not be null: apiKey");
    }

    public URL getApiUrl() {
        return apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getUrlEncoding() {
        return urlEncoding;
    }

    public void setUrlEncoding(String urlEncoding) {
        this.urlEncoding = urlEncoding;
    }

    public Oembed.OembedBuilder oembed() {
        return Oembed.withEmbedly(this);
    }

    public static void main(String... arguments) throws MalformedURLException {
        Embedly embedly = new Embedly(new URL("https://api.embed.ly/1/oembed?#abc"), "936d6a99e233444fb8c25ac1368c2618");
        Oembed oembed = embedly.oembed()
                .withUrl(new URL("http://www.adressa.no/bolig/article560853.snd"))
                .withNostyle(true)
                .build();

        try (Http.HttpRequest request = oembed.getHttpRequest()) {
            System.out.println(request.getUrl());
            try (Http.HttpResponse response = request.getResponse()) {
                try (InputStream entity = response.getInputStream()) {
                    System.out.println(StreamUtils.copyToString(entity, Charset.forName("UTF-8")));
                }
            }
        } catch (Exception cause) {
            cause.printStackTrace();
        }
    }


}
