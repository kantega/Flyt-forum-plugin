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
@XmlType(name = "ForumReferences")
@XmlRootElement(name = "forums")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({ForumReferenceTo.class})
public class ForumReferencesTo extends ArrayList<ForumReferenceTo> {

    public ForumReferencesTo(int initialCapacity) {
        super(initialCapacity);
    }

    public ForumReferencesTo() {
    }

    public ForumReferencesTo(Collection c) {
        super(c);
    }

    @XmlElement(name = "forum")
    public List<ForumReferenceTo> getUsers() {
        return this;
    }
}

