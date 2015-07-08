package no.kantega.forum.jaxrs.tol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-06
 */
@XmlType(name = "Group")
@XmlRootElement(name = "group")
@XmlAccessorType(XmlAccessType.NONE)
public class GroupTo {

    @XmlElement(name = "id")
    private String id;
    @XmlElement(name = "reference")
    private List<ResourceReferenceTo> references;

    public GroupTo() {
    }

    public GroupTo(String id, List<ResourceReferenceTo> references) {
        this.id = id;
        this.references = references;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ResourceReferenceTo> getReferences() {
        return references;
    }

    public void setReferences(List<ResourceReferenceTo> references) {
        this.references = references;
    }
}
