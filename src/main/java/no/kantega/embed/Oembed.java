package no.kantega.embed;

import no.kantega.utilities.Http;
import no.kantega.utilities.Objects;
import no.kantega.utilities.Url;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static no.kantega.utilities.Url.createUrl;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-01
 */
public class Oembed {

    private Embedly embedly;
    private List<URL> urls;
    private Integer maxWitdth;
    private Integer maxHeight;
    private Integer width;
    private Boolean nostyle;
    private Boolean autoplay;
    private Boolean videosrc;
    private Integer words;
    private Integer chars;
    private Boolean luxe;
    private Boolean secure;
    private Scheme scheme;

    private Oembed(Embedly embedly, List<URL> urls, Integer maxWitdth, Integer maxHeight, Integer width, Boolean nostyle, Boolean autoplay, Boolean videosrc, Integer words, Integer chars, Boolean luxe, Boolean secure, Scheme scheme) {
        this.embedly = Objects.requireNonNull(embedly, "May not be null: embedly");
        this.urls = new ArrayList<>(
                Objects.requireNonNullElements(
                        Objects.requireNonEmpty(
                                Objects.requireNonNull(urls, "May not be null: urls"),
                                "May not be empty: urls"),
                        "May not be null: urls[%d]"
                )
        );
        this.maxWitdth = maxWitdth;
        this.maxHeight = maxHeight;
        this.width = width;
        this.nostyle = nostyle;
        this.autoplay = autoplay;
        this.videosrc = videosrc;
        this.words = words;
        this.chars = chars;
        this.luxe = luxe;
        this.secure = secure;
        this.scheme = scheme;
    }

    public Embedly getEmbedly() {
        return embedly;
    }

    public List<URL> getUrls() {
        return urls;
    }

    public Integer getMaxWitdth() {
        return maxWitdth;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public Integer getWidth() {
        return width;
    }

    public Boolean getNostyle() {
        return nostyle;
    }

    public Boolean getAutoplay() {
        return autoplay;
    }

    public Boolean getVideosrc() {
        return videosrc;
    }

    public Integer getWords() {
        return words;
    }

    public Integer getChars() {
        return chars;
    }

    public Boolean getLuxe() {
        return luxe;
    }

    public Boolean getSecure() {
        return secure;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public static OembedBuilder withEmbedly(Embedly embedly) {
        return new DefalutOembedBuilder().withEmbedly(embedly);
    }

    private URL toUrl() {
        URL url = this.getEmbedly().getApiUrl();
        String query = Http.query(url.getQuery(), embedly.getApiUrlEncoding())
                .put("key", this.getEmbedly().getApiKey())
                .put(new Http.DecodedNameEncodedValuePair("urls", Url.getEncodedUrls(this.getUrls(), embedly.getApiUrlEncoding()), embedly.getApiUrlEncoding()))
                .put("maxwidth", this.getMaxWitdth())
                .put("maxheight", this.getMaxHeight())
                .put("width", this.getWidth())
                .put("nostyle", this.getNostyle())
                .put("autoplay", this.getAutoplay())
                .put("videosrc", this.getVideosrc())
                .put("words", this.getWords())
                .put("chars", this.getChars())
                .put("luxe", this.getLuxe())
                .put("secure", this.getSecure())
                .put("scheme", this.getScheme())
                .build();
        return createUrl(url, query, url.getRef());
    }

    public Http.HttpRequest getHttpRequest() {
        return new Http.DefaultHttpRequest(
                "GET",
                toUrl(),
                Http.DefaultHttpHeadersBuilder.builder()
                        .withHeader("Accept", "application/json")
                        .build()
        );
    }

    public enum Scheme {
        http,
        https;
    }

    public interface OembedBuilder {

        OembedBuilder withUrl(URL url);

        OembedBuilder withoutUrl(URL url);

        OembedBuilder withMaxWitdth(Integer maxWitdth);

        OembedBuilder withMaxHeight(Integer maxHeight);

        OembedBuilder withWidth(Integer width);

        OembedBuilder withNostyle(Boolean nostyle);

        OembedBuilder withAutoplay(Boolean autoplay);

        OembedBuilder withVideosrc(Boolean videosrc);

        OembedBuilder withWords(Integer words);

        OembedBuilder withChars(Integer chars);

        OembedBuilder withLuxe(Boolean luxe);

        OembedBuilder withSecure(Boolean secure);

        OembedBuilder withScheme(Scheme scheme);

        Oembed build();
    }

    public static class DefalutOembedBuilder implements OembedBuilder {

        private Embedly embedly;
        private List<URL> urls;
        private Integer maxWitdth;
        private Integer maxHeight;
        private Integer width;
        private Boolean nostyle;
        private Boolean autoplay;
        private Boolean videosrc;
        private Integer words;
        private Integer chars;
        private Boolean luxe;
        private Boolean secure;
        private Scheme scheme;

        public OembedBuilder withEmbedly(Embedly embedly) {
            this.embedly = embedly;
            return this;
        }

        public OembedBuilder withUrl(URL url) {
            if (url != null) {
                if (urls == null) {
                    urls = new ArrayList<>();
                }
                urls.add(url);
            }
            return this;
        }

        public OembedBuilder withoutUrl(URL url) {
            if (url != null) {
                if (urls != null) {
                    urls.remove(url);
                    if (urls.isEmpty()) {
                        urls = null;
                    }
                }
            }
            return this;
        }

        public OembedBuilder withMaxWitdth(Integer maxWitdth) {
            this.maxWitdth = maxWitdth;
            return this;
        }

        public OembedBuilder withMaxHeight(Integer maxHeight) {
            this.maxHeight = maxHeight;
            return this;
        }

        public OembedBuilder withWidth(Integer width) {
            this.width = width;
            return this;
        }

        public OembedBuilder withNostyle(Boolean nostyle) {
            this.nostyle = nostyle;
            return this;
        }

        public OembedBuilder withAutoplay(Boolean autoplay) {
            this.autoplay = autoplay;
            return this;
        }

        public OembedBuilder withVideosrc(Boolean videosrc) {
            this.videosrc = videosrc;
            return this;
        }

        public OembedBuilder withWords(Integer words) {
            this.words = words;
            return this;
        }

        public OembedBuilder withChars(Integer chars) {
            this.chars = chars;
            return this;
        }

        public OembedBuilder withLuxe(Boolean luxe) {
            this.luxe = luxe;
            return this;
        }

        public OembedBuilder withSecure(Boolean secure) {
            this.secure = secure;
            return this;
        }

        public OembedBuilder withScheme(Scheme scheme) {
            this.scheme = scheme;
            return this;
        }

        public Oembed build() {
            return new Oembed(embedly, urls, maxWitdth, maxHeight, width, nostyle, autoplay, videosrc, words, chars, luxe, secure, scheme);
        }

    }

}
