package no.kantega.forum.jaxrs.tol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.net.URI;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@XmlType(name = "ThreadReference")
@XmlAccessorType(XmlAccessType.NONE)
public class ThreadReferenceTo extends ResourceReferenceTo {

    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "description")
    private String description;

    public ThreadReferenceTo() {
    }

    public ThreadReferenceTo(Long id, String name, String description, URI href, String rel, String title, ResourceReferenceTo targetSchema, String mediaType, String method, String encType, ResourceReferenceTo schema) {
        super(href, rel, title, targetSchema, mediaType, method, encType, schema);
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
