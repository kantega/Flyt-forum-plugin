package no.kantega.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static no.kantega.utilities.Url.*;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-02
 */
public class Http {

    private Http() {}

    public static HttpHeadersBuilder headers() {
        return new DefaultHttpHeadersBuilder(null);
    }

    public static DefaultQueryBuilder query(String query, String urlEncoding) {
        return new DefaultQueryBuilder(query, urlEncoding);
    }

    public static DefaultQueryBuilder forEncoding(String urlEncoding) {
        return new DefaultQueryBuilder(null, urlEncoding);
    }

    public interface QueryBuilder {


        public QueryBuilder removeAll(String name);

        public QueryBuilder add(NameValuePair nameValuePair);

        public <T> QueryBuilder add(String name, T value);

        public QueryBuilder put(NameValuePair nameValuePair);

        public <T> QueryBuilder put(String name, T value);

        public String build();

        interface NameValuePair<T> {
            String getName();
            T getValue();
            String getEncodedName();
            String getEncodedValue();
        }
    }

    public interface HttpHeadersBuilder {
        HttpHeadersBuilder withHeader(HttpHeader httpHeader);
        HttpHeadersBuilder withHeader(String name, String value);
        Map<String,List<String>> build();

        interface HttpHeader {
            String getName();
            String getValue();
        }
    }

    public interface HttpRequest extends AutoCloseable {
        String getMethod();
        URL getUrl();
        Map<String,List<String>> getHeaders();
        OutputStream getOutputStream() throws IOException;
        HttpResponse getResponse() throws IOException;
    }

    public interface HttpResponse extends AutoCloseable {
        int getResponseCode();
        URL getUrl();
        Map<String,List<String>> getHeaders();
        InputStream getInputStream();
    }

    public static class DefaultHttpHeadersBuilder implements HttpHeadersBuilder {

        private List<HttpHeadersBuilder.HttpHeader> httpHeaders;

        private DefaultHttpHeadersBuilder(List<HttpHeadersBuilder.HttpHeader> httpHeaders) {
            this.httpHeaders = httpHeaders;
        }

        public static DefaultHttpHeadersBuilder builder() {
            return new DefaultHttpHeadersBuilder(null);
        }

        public DefaultHttpHeadersBuilder withHeader(HttpHeadersBuilder.HttpHeader httpHeader) {
            if (Objects.isNull(httpHeaders)) {
                httpHeaders = new ArrayList<>();
            }
            httpHeaders.add(httpHeader);
            return this;
        }

        public DefaultHttpHeadersBuilder withHeader(String name, String value) {
            return withHeader(new DefaultHttpHeader(name, value));
        }

        public Map<String,List<String>> build() {
            if (Objects.nonNull(httpHeaders)) {
                Map<String,List<String>> hs = new LinkedHashMap<>(httpHeaders.size());
                for (HttpHeader httpHeader : httpHeaders) {
                    List<String> h = hs.get(httpHeader.getName());
                    if (Objects.isNull(h)) {
                        h = new ArrayList<>();
                        hs.put(httpHeader.getName(), h);
                    }
                    h.add(httpHeader.getValue());
                 }
            }
            return null;
        }
    }

    public static class DefaultHttpHeader implements HttpHeadersBuilder.HttpHeader {
        private String name;
        private String value;

        public DefaultHttpHeader(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * @author Kristian Myrhaug
     * @since 2015-07-01
     */
    public static class DefaultQueryBuilder {

        public static final String PARAMETER_SEPARATOR = "&";
        public static final String NAME_VALUE_SEPARATOR = "=";

        private List<QueryBuilder.NameValuePair> nameValuePairs;
        private String encoding;

        private DefaultQueryBuilder(String query, String urlEncoding) {
            if (Objects.nonNull(query)) {
                for (String pair : query.split(PARAMETER_SEPARATOR)) {
                    String[] nameValue = pair.split(NAME_VALUE_SEPARATOR);
                    if (nameValue.length == 2) {
                        nameValuePairs = getOrCreate(nameValuePairs);
                        nameValuePairs.add(new EncodedValuePair(nameValue[0], nameValue[1], urlEncoding));
                    }
                }
            }
            this.encoding = urlEncoding;
        }

        public DefaultQueryBuilder removeAll(String name) {
            if (Objects.nonNull(nameValuePairs)) {
                for (int index = 0; index < nameValuePairs.size(); index++) {
                    if (name.equals(nameValuePairs.get(index).getName())) {
                        nameValuePairs.remove(index--);
                    }
                }
                nameValuePairs = getOrDelete(nameValuePairs);
            }
            return this;
        }

        public DefaultQueryBuilder add(QueryBuilder.NameValuePair nameValuePair) {
            nameValuePairs = getOrCreate(nameValuePairs);
            nameValuePairs.add(nameValuePair);
            return this;
        }

        public <T> DefaultQueryBuilder add(String name, T value) {
            return add(new DecodedNameValuePair<>(name, value, encoding));
        }

        public DefaultQueryBuilder put(QueryBuilder.NameValuePair nameValuePair) {
            nameValuePairs = getOrCreate(nameValuePairs);
            removeAll(nameValuePair.getName());
            add(nameValuePair);
            return this;
        }

        public <T> DefaultQueryBuilder put(String name, T value) {
            return put(new DecodedNameValuePair<>(name, value, encoding));
        }

        public String build() {
            if (Objects.nonNull(nameValuePairs) && !nameValuePairs.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (QueryBuilder.NameValuePair nameValuePair : nameValuePairs) {
                    if (Objects.nonNull(nameValuePair.getEncodedName()) && Objects.nonNull(nameValuePair.getEncodedValue())) {
                        if (sb.length() > 0) {
                            sb.append(PARAMETER_SEPARATOR);
                        }
                        sb.append(nameValuePair.getEncodedName()).append(NAME_VALUE_SEPARATOR).append(nameValuePair.getEncodedValue());
                    }
                }
                return sb.toString();
            }
            return null;
        }

        private static <T> List<T> getOrCreate(List<T> list) {
            return Objects.nonNull(list) ? list : new ArrayList<T>();
        }

        private static <T> List<T> getOrDelete(List<T> list) {
            return Objects.isNull(list) || list.isEmpty() ? null : list;
        }
    }


    public static class DecodedNameValuePair<T> implements QueryBuilder.NameValuePair<T> {

        private String name;
        private T value;
        private String encoding;
        private String encodedName;
        private String encodedValue;

        public DecodedNameValuePair(String name, T value, String encoding) {
            this.name = name;
            this.value = value;
            this.encoding = encoding;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public String getEncodedName() {
            if (Objects.isNull(encodedName)) {
                encodedName = Objects.nonNull(name) ? urlEncode(name, encoding) : null;
            }
            return encodedName;
        }

        @Override
        public String getEncodedValue() {
            if (Objects.isNull(encodedValue)) {
                encodedValue = Objects.nonNull(value) ? urlEncode(value, encoding) : null;
            }
            return encodedValue;
        }
    }

    public static class EncodedValuePair implements QueryBuilder.NameValuePair<String> {

        private String name;
        private String value;
        private String encoding;
        private String decodedName;
        private String decodedValue;


        public EncodedValuePair(String name, String value, String encoding) {
            this.name = name;
            this.value = value;
            this.encoding = encoding;
        }

        @Override
        public String getName() {
            if (Objects.isNull(decodedName)) {
                decodedName = Objects.nonNull(name) ? urlDecode(name, encoding) : null;
            }
            return decodedName;
        }

        @Override
        public String getValue() {
            if (Objects.isNull(decodedValue)) {
                decodedValue = Objects.nonNull(value) ? urlDecode(value, encoding) : null;
            }
            return decodedValue;
        }

        @Override
        public String getEncodedName() {
            return name;
        }

        @Override
        public String getEncodedValue() {
            return value;
        }
    }

    public static class DecodedNameEncodedValuePair implements QueryBuilder.NameValuePair<String> {

        private String name;
        private String value;
        private String encoding;
        private String encodedName;
        private String decodedValue;

        public DecodedNameEncodedValuePair(String name, String value, String encoding) {
            this.name = name;
            this.value = value;
            this.encoding = encoding;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getValue() {
            if (Objects.isNull(decodedValue)) {
                decodedValue = Objects.nonNull(value) ? urlDecode(value, encoding) : null;
            }
            return decodedValue;
        }

        @Override
        public String getEncodedName() {
            if (Objects.isNull(encodedName)) {
                encodedName = Objects.nonNull(name) ? urlEncode(name, encoding) : null;
            }
            return encodedName;
        }

        @Override
        public String getEncodedValue() {
            return value;
        }
    }

    public static class DefaultHttpRequest implements HttpRequest {

        private String method;
        private URL url;
        private Map<String,List<String>> headers;
        private HttpURLConnection connection;
        private HttpResponse response;

        public DefaultHttpRequest(String method, URL url, Map<String, List<String>> headers) {
            this.method = method;
            this.url = url;
            this.headers = headers;
        }

        @Override
        public String getMethod() {
            return Objects.nonNull(method) ? method : Objects.nonNull(connection) ? connection.getRequestMethod() : null;
        }

        @Override
        public URL getUrl() {
            return url;
        }

        @Override
        public Map<String, List<String>> getHeaders() {
            return Objects.nonNull(headers) ? headers : Objects.nonNull(connection) ? connection.getRequestProperties() : null;
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            HttpURLConnection connection = getConnection();
            connection.setDoOutput(true);
            return connection.getOutputStream();
        }

        @Override
        public HttpResponse getResponse() throws IOException {
            if (Objects.isNull(response)) {
                response = new DefaultHttpResponse(getConnection());
            }
            return response;
        }

        private HttpURLConnection getConnection() throws IOException {
            if (Objects.isNull(connection)) {
                connection = (HttpURLConnection) url.openConnection();
                if (Objects.nonNull(method)) {
                    connection.setRequestMethod(method);
                }
                if (Objects.nonNull(headers)) {
                    for (Map.Entry<String,List<String>> entry : headers.entrySet()) {
                        for (String value : entry.getValue()) {
                            connection.setRequestProperty(entry.getKey(), value);
                        }
                    }
                }
                connection.setDoInput(true);
            }
            return connection;
        }

        @Override
        public void close() throws Exception {
            try {
                if (Objects.nonNull(response)) {
                    response.close();
                }
            } finally {
                try {
                    if (Objects.nonNull(connection)) {
                        connection.disconnect();
                    }
                } finally {
                    response = null;
                    connection = null;
                }
            }

        }
    }

    public static class DefaultHttpResponse implements HttpResponse {

        private HttpURLConnection connection;
        private int responseCode = -1;
        private URL url;
        private Map<String,List<String>> headers;
        private InputStream inputStream;

        public DefaultHttpResponse(HttpURLConnection connection) {
            this.connection = connection;
            try {
                this.responseCode = connection.getResponseCode();
            } catch (IOException cause) {
                inputStream = connection.getErrorStream();
            }
            url = connection.getURL();
            headers = connection.getHeaderFields();
        }

        @Override
        public int getResponseCode() {
            return responseCode;
        }

        @Override
        public URL getUrl() {
            return url;
        }

        @Override
        public Map<String, List<String>> getHeaders() {
            return headers;
        }

        @Override
        public InputStream getInputStream() {
            if (Objects.isNull(inputStream)) {
                try {
                    int responseCode = connection.getResponseCode();
                    if (200 <= responseCode && responseCode < 300) {
                        inputStream = connection.getInputStream();
                    } else {
                        inputStream = connection.getErrorStream();
                        if (Objects.isNull(inputStream)) {
                            connection.getInputStream();
                        }
                    }
                } catch (IOException cause) {
                    inputStream = connection.getErrorStream();
                }
            }
            return inputStream;
        }

        @Override
        public void close() throws Exception {
            try {
                if (Objects.nonNull(inputStream)) {
                    inputStream.close();
                }
            } finally {
                try {
                    if (Objects.nonNull(connection)) {
                        connection.disconnect();
                    }
                } finally {
                    inputStream = null;
                    headers = null;
                    url = null;
                    responseCode = -1;
                    connection = null;
                }
            }
        }
    }
}
