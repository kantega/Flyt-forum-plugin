package no.kantega.forum.jaxrs.tol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@XmlType(name = "Fault")
@XmlRootElement(name = "fault")
@XmlAccessorType(XmlAccessType.NONE)
public class FaultTo extends ArrayList<String> {
    public FaultTo(int initialCapacity) {
        super(initialCapacity);
    }

    public FaultTo() {
    }

    public FaultTo(Collection<? extends String> c) {
        super(c);
    }

    @XmlElement(name = "message")
    public List<String> getMessages() {
        return this;
    }
}

