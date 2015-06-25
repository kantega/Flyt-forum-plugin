package no.kantega.forum.jaxrs.tol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@XmlType(name = "ResourceReferences")
@XmlRootElement(name = "resources")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({ResourceReferenceTo.class})
public class ResourceReferencesTo extends ArrayList<ResourceReferenceTo> {

    public ResourceReferencesTo(int initialCapacity) {
        super(initialCapacity);
    }

    public ResourceReferencesTo() {
    }

    public ResourceReferencesTo(Collection c) {
        super(c);
    }

    @XmlElement(name = "resource")
    public List<ResourceReferenceTo> getUsers() {
        return this;
    }
}

