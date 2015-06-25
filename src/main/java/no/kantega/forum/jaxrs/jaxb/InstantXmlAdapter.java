package no.kantega.forum.jaxrs.jaxb;

import org.joda.time.Instant;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
public class InstantXmlAdapter extends XmlAdapter<String, Instant> {

    @Override
    public Instant unmarshal(String marshalled) throws Exception {
        return staticUnmarshal(marshalled);
    }

    @Override
    public String marshal(Instant unmarshalled) throws Exception {
        return staticMarshal(unmarshalled);
    }

    public static Instant staticUnmarshal(String marshalled) throws Exception {
        return marshalled != null ? Instant.parse(marshalled) : null;
    }

    public static String staticMarshal(Instant unmarshalled) throws Exception {
        return unmarshalled != null ? unmarshalled.toString() : null;
    }
}
