package no.kantega.embed;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-01
 */
public class Utilities {

    private Utilities() {}

    public static <T> T requireNonNull(T object, String message) {
        if (object == null)
            throw new NullPointerException(message);
        return object;
    }

    public static <T extends Collection> T requireNonEmpty(T object, String message) {
        if (object.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return object;
    }

    public static <T extends Collection> T requireNonNullElements(T object, String format) {
        int index = 0;
        for (Object element : object) {
            if (element == null) {
                throw new NullPointerException(String.format(format, index));
            }
            index++;
        }
        return object;
    }

    public static boolean nonNull(Object object) {
        return object != null;
    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static String urlEncode(Object string, String encoding) {
        try {
            return URLEncoder.encode(String.valueOf(string), encoding);
        } catch (UnsupportedEncodingException cause) {
            throw new RuntimeException(String.format("Unsupported encoding: %s", encoding), cause);
        }
    }

    public static String urlDecode(String string, String encoding) {
        try {
            return URLDecoder.decode(string, encoding);
        } catch (UnsupportedEncodingException cause) {
            throw new RuntimeException(String.format("Unsupported encoding: %s", encoding), cause);
        }
    }

    public static URL createUrl(String string) {
        try {
            return new URL(string);
        } catch (MalformedURLException cause) {
            throw new RuntimeException("Could not create URL", cause);
        }
    }

    public static URL createUrl(URL url, String query, String fragment) {
        String string = url.toString();
        int index = string.indexOf("#");
        if (index > -1) {
            string.substring(0, index);
        }
        index = string.indexOf("?");
        if (index > -1) {
            string.substring(0, index);
        }
        return createUrl(string + (nonNull(query) ? "?" + query : "") + (nonNull(fragment) ? "#" + fragment : ""));
    }

    public static String getEncodedUrls(Collection<URL> urls, String encoding) {
        StringBuilder stringBuilder = new StringBuilder();
        for (URL url : urls) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(urlEncode(url.toString(), encoding));
        }
        return stringBuilder.toString();
    }
}
