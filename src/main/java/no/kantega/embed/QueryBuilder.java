package no.kantega.embed;

import java.util.ArrayList;
import java.util.List;

import static no.kantega.embed.Utilities.isNull;
import static no.kantega.embed.Utilities.nonNull;
import static no.kantega.embed.Utilities.urlDecode;
import static no.kantega.embed.Utilities.urlEncode;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-01
 */
public class QueryBuilder {

    public static final String PARAMETER_SEPARATOR = "&";
    public static final String NAME_VALUE_SEPARATOR = "=";

    private List<NameValuePair> nameValuePairs;
    private String encoding;

    private QueryBuilder(String query, String encoding) {
        if (nonNull(query)) {
            for (String pair : query.split(PARAMETER_SEPARATOR)) {
                String[] nameValue = pair.split(NAME_VALUE_SEPARATOR);
                if (nameValue.length == 2) {
                    nameValuePairs = getOrCreate(nameValuePairs);
                    nameValuePairs.add(new EncodedValuePair(nameValue[0], nameValue[1], encoding));
                }
            }
        }
        this.encoding = encoding;
    }

    public QueryBuilder removeAll(String name) {
        if (nonNull(nameValuePairs)) {
            for (int index = 0; index < nameValuePairs.size(); index++) {
                if (name.equals(nameValuePairs.get(index).getName())) {
                    nameValuePairs.remove(index--);
                }
            }
            nameValuePairs = getOrDelete(nameValuePairs);
        }
        return this;
    }

    public QueryBuilder add(NameValuePair nameValuePair) {
        nameValuePairs = getOrCreate(nameValuePairs);
        nameValuePairs.add(nameValuePair);
        return this;
    }

    public QueryBuilder add(String name, String value) {
        return add(new DecodedNameValuePair<String>(name, value, encoding));
    }

    public QueryBuilder put(NameValuePair nameValuePair) {
        nameValuePairs = getOrCreate(nameValuePairs);
        removeAll(nameValuePair.getName());
        add(nameValuePair);
        return this;
    }

    public <T> QueryBuilder put(String name, T value) {
        return put(new DecodedNameValuePair<T>(name, value, encoding));
    }

    public String build() {
        if (nonNull(nameValuePairs) && !nameValuePairs.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (NameValuePair nameValuePair : nameValuePairs) {
                if (nonNull(nameValuePair.getEncodedName()) && nonNull(nameValuePair.getEncodedValue())) {
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

    public static QueryBuilder forQuery(String query, String encoding) {
        return new QueryBuilder(query, encoding);
    }

    public static QueryBuilder forEncoding(String encoding) {
        return new QueryBuilder(null, encoding);
    }

    private static <T> List<T> getOrCreate(List<T> list) {
        return nonNull(list) ? list : new ArrayList<T>();
    }

    private static <T> List<T> getOrDelete(List<T> list) {
        return isNull(list) || list.isEmpty() ? null : list;
    }

    public interface NameValuePair<T> {
        String getName();
        T getValue();
        String getEncodedName();
        String getEncodedValue();
    }

    public static class DecodedNameValuePair<T> implements NameValuePair<T> {

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
            if (isNull(encodedName)) {
                encodedName = nonNull(name) ? urlEncode(name, encoding) : null;
            }
            return encodedName;
        }

        @Override
        public String getEncodedValue() {
            if (isNull(encodedValue)) {
                encodedValue = nonNull(value) ? urlEncode(value, encoding) : null;
            }
            return encodedValue;
        }
    }

    public static class EncodedValuePair implements NameValuePair<String> {

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
            if (isNull(decodedName)) {
                decodedName = nonNull(name) ? urlDecode(name, encoding) : null;
            }
            return decodedName;
        }

        @Override
        public String getValue() {
            if (isNull(decodedValue)) {
                decodedValue = nonNull(value) ? urlDecode(value, encoding) : null;
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

    public static class DecodedNameEncodedValuePair implements NameValuePair<String> {

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
            if (isNull(decodedValue)) {
                decodedValue = nonNull(value) ? urlDecode(value, encoding) : null;
            }
            return decodedValue;
        }

        @Override
        public String getEncodedName() {
            if (isNull(encodedName)) {
                encodedName = nonNull(name) ? urlEncode(name, encoding) : null;
            }
            return encodedName;
        }

        @Override
        public String getEncodedValue() {
            return value;
        }
    }
}
