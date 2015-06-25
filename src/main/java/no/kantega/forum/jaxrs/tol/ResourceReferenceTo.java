package no.kantega.forum.jaxrs.tol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.net.URI;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@XmlType(name = "ResourceReference")
@XmlAccessorType(XmlAccessType.NONE)
public class ResourceReferenceTo {

    @XmlAttribute(required = true)
    private URI href;
    @XmlAttribute(required = true)
    private String rel;
    @XmlAttribute
    private String title;
    @XmlElement
    private ResourceReferenceTo targetSchema;
    @XmlAttribute
    private String mediaType;
    @XmlAttribute
    private String method;
    @XmlAttribute
    private String encType;
    @XmlElement
    private ResourceReferenceTo schema;

    public ResourceReferenceTo() {
    }

    public ResourceReferenceTo(URI href, String rel, String title, ResourceReferenceTo targetSchema, String mediaType, String method, String encType, ResourceReferenceTo schema) {
        this.rel = rel;
        this.href = href;
        this.title = title;
        this.targetSchema = targetSchema;
        this.mediaType = mediaType;
        this.method = method;
        this.encType = encType;
        this.schema = schema;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public URI getHref() {
        return href;
    }

    public void setHref(URI href) {
        this.href = href;
    }
}

