package no.kantega.embed;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.MalformedURLException;
import java.net.URL;

import static no.kantega.embed.Utilities.requireNonNull;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-01
 */
public class Embedly {

    public static final String DEFAULT_URL_ENCODING = "UTF-8";

    private URL apiUrl;
    private String apiKey;
    private String encoding = DEFAULT_URL_ENCODING;

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

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
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
        System.out.println(oembed.toUrl());
    }


}
