package no.kantega.forum.jaxrs.tol;

import no.kantega.forum.jaxrs.jaxb.InstantXmlAdapter;
import no.kantega.forum.model.Attachment;
import org.joda.time.Instant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.InputStream;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-26
 */
@XmlType(name = "Attachment")
@XmlRootElement(name = "attachment")
@XmlAccessorType(XmlAccessType.NONE)
public class AttachmentTo {

    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "fileName")
    private String fileName;
    @XmlElement(name = "fileSize")
    private Long fileSize;
    @XmlElement(name = "mimeType")
    private String mimeType;
    @XmlElement(name = "dataReference")
    private ResourceReferenceTo dataReference;
    private InputStream data;
    @XmlElement(name = "created")
    @XmlJavaTypeAdapter(InstantXmlAdapter.class)
    private Instant created;
    @XmlElement(name = "postReference")
    private ResourceReferenceTo postReference;

    public AttachmentTo() {
    }

    public AttachmentTo(String fileName, Long fileSize, String mimeType, InputStream data, Instant created) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.data = data;
        this.created = created;
    }

    public AttachmentTo(Attachment attachment, ResourceReferenceTo dataReference, ResourceReferenceTo postReference) {
        this.id = attachment.getId();
        this.fileName = attachment.getFileName();
        this.fileSize = attachment.getFileSize();
        this.mimeType = attachment.getMimeType();
        this.dataReference = dataReference;
        this.created = attachment.getCreated() != null ? new Instant(attachment.getCreated().getTime()) : null;
        this.postReference = postReference;
    }

    public AttachmentTo(Long id, String fileName, Long fileSize, String mimeType, ResourceReferenceTo dataReference, Instant created, ResourceReferenceTo postReference) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.dataReference = dataReference;
        this.created = created;
        this.postReference = postReference;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public ResourceReferenceTo getDataReference() {
        return dataReference;
    }

    public void setDataReference(ResourceReferenceTo dataReference) {
        this.dataReference = dataReference;
    }

    public InputStream getData() {
        return data;
    }

    public void setData(InputStream data) {
        this.data = data;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public ResourceReferenceTo getPostReference() {
        return postReference;
    }

    public void setPostReference(ResourceReferenceTo postReference) {
        this.postReference = postReference;
    }
}
