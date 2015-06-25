package no.kantega.forum.jaxrs.tol;

import no.kantega.forum.jaxrs.jaxb.InstantXmlAdapter;
import no.kantega.forum.model.Forum;
import org.joda.time.Instant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@XmlType(name = "Forum")
@XmlRootElement(name = "forum")
@XmlAccessorType(XmlAccessType.NONE)
public class ForumTo {

    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "numThreads")
    private Integer numThreads;
    @XmlElement(name = "id")
    @XmlJavaTypeAdapter(InstantXmlAdapter.class)
    private Instant createdDate;
    @XmlElement(name = "owner")
    private String owner;
    @XmlElement(name = "anonymousPostAllowed")
    private Boolean anonymousPostAllowed = true;
    @XmlElement(name = "attachmentsAllowed")
    private Boolean attachmentsAllowed = false;
    @XmlElement(name = "approvalRequired")
    private Boolean approvalRequired = false;
    @XmlElement(name = "moderator")
    private String moderator;
    @XmlElement(name = "categoryReference")
    private CategoryReferenceTo categoryReference;
    @XmlElementWrapper(name = "actions")
    @XmlElement(name = "action")
    private List<ResourceReferenceTo> actions;

    //private Set<ForumThread> threads;
    //private Set<String> groups;


    public ForumTo() {
    }

    public ForumTo(Forum forumBo, CategoryReferenceTo categoryReference, List<ResourceReferenceTo> actions) {
        this.id = forumBo.getId();
        this.name = forumBo.getName();
        this.description = forumBo.getDescription();
        this.numThreads = forumBo.getNumThreads();
        this.createdDate = forumBo.getCreatedDate() != null ? new Instant(forumBo.getCreatedDate().getTime()) : null;
        this.owner = forumBo.getOwner();
        this.anonymousPostAllowed = forumBo.isAnonymousPostAllowed();
        this.attachmentsAllowed = forumBo.isAttachmentsAllowed();
        this.approvalRequired = forumBo.isApprovalRequired();
        this.moderator = forumBo.getModerator();
        this.categoryReference = categoryReference;
        this.actions = actions;
    }

    public ForumTo(Long id, String name, String description, Integer numThreads, Instant createdDate, String owner, Boolean anonymousPostAllowed, Boolean attachmentsAllowed, Boolean approvalRequired, String moderator, CategoryReferenceTo categoryReference, List<ResourceReferenceTo> actions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numThreads = numThreads;
        this.createdDate = createdDate;
        this.owner = owner;
        this.anonymousPostAllowed = anonymousPostAllowed;
        this.attachmentsAllowed = attachmentsAllowed;
        this.approvalRequired = approvalRequired;
        this.moderator = moderator;
        this.categoryReference = categoryReference;
        this.actions = actions;
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

    public Integer getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(Integer numThreads) {
        this.numThreads = numThreads;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getAnonymousPostAllowed() {
        return anonymousPostAllowed;
    }

    public void setAnonymousPostAllowed(Boolean anonymousPostAllowed) {
        this.anonymousPostAllowed = anonymousPostAllowed;
    }

    public Boolean getAttachmentsAllowed() {
        return attachmentsAllowed;
    }

    public void setAttachmentsAllowed(Boolean attachmentsAllowed) {
        this.attachmentsAllowed = attachmentsAllowed;
    }

    public Boolean getApprovalRequired() {
        return approvalRequired;
    }

    public void setApprovalRequired(Boolean approvalRequired) {
        this.approvalRequired = approvalRequired;
    }

    public String getModerator() {
        return moderator;
    }

    public void setModerator(String moderator) {
        this.moderator = moderator;
    }

    public CategoryReferenceTo getCategoryReference() {
        return categoryReference;
    }

    public void setCategoryReference(CategoryReferenceTo categoryReference) {
        this.categoryReference = categoryReference;
    }

    public List<ResourceReferenceTo> getActions() {
        return actions;
    }

    public void setActions(List<ResourceReferenceTo> actions) {
        this.actions = actions;
    }
}
