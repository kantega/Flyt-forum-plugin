package no.kantega.forum.jaxrs.tol;

import no.kantega.forum.jaxrs.jaxb.InstantXmlAdapter;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import org.joda.time.Instant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.Set;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@XmlType(name = "Thread")
@XmlRootElement(name = "thread")
@XmlAccessorType(XmlAccessType.NONE)
public class ThreadTo {

    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "createdDate")
    @XmlJavaTypeAdapter(InstantXmlAdapter.class)
    private Instant createdDate;
    @XmlElement(name = "numPosts")
    private Integer numPosts;
    @XmlElement(name = "owner")
    private String owner;
    @XmlElement(name = "contentId")
    private Integer contentId;
    @XmlElement(name = "approved")
    private Boolean approved;
    @XmlElement(name = "lastPostDate")
    @XmlJavaTypeAdapter(InstantXmlAdapter.class)
    private Instant lastPostDate;
    @XmlElement(name = "modifiedDate")
    @XmlJavaTypeAdapter(InstantXmlAdapter.class)
    private Instant modifiedDate;
    @XmlElement(name = "forumReference")
    private ForumReferenceTo forumReference;
    @XmlElementWrapper(name = "posts")
    @XmlElement(name = "post")
    private List<PostTo> posts;
    @XmlElementWrapper(name = "actions")
    @XmlElement(name = "action")
    private List<ResourceReferenceTo> actions;
    /*
    @XmlElement(name = "posts")
    private Set<Post> posts;
    @XmlElement(name = "topics")
    private Set topics;*/

    public ThreadTo() {
    }

    public ThreadTo(ForumThread threadBo, ForumReferenceTo forumReference, List<PostTo> posts, List<ResourceReferenceTo> actions) {
        this.id = threadBo.getId();
        this.name = threadBo.getName();
        this.description = threadBo.getDescription();
        this.createdDate = threadBo.getCreatedDate() != null ? new Instant(threadBo.getCreatedDate().getTime()) : null;
        this.numPosts = threadBo.getNumPosts();
        this.owner = threadBo.getOwner();
        this.contentId = threadBo.getContentId();
        this.approved = threadBo.isApproved();
        this.lastPostDate = threadBo.getLastPostDate() != null ? new Instant(threadBo.getLastPostDate().getTime()) : null;
        this.modifiedDate = threadBo.getModifiedDate() != null ? new Instant(threadBo.getModifiedDate()) : null;
        this.forumReference = forumReference;
        this.posts = posts;
        this.actions = actions;
    }

    public ThreadTo(Long id, String name, String description, Instant createdDate, Integer numPosts, String owner, Integer contentId, Boolean approved, Instant lastPostDate, Instant modifiedDate, ForumReferenceTo forumReference, List<PostTo> posts, List<ResourceReferenceTo> actions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.numPosts = numPosts;
        this.owner = owner;
        this.contentId = contentId;
        this.approved = approved;
        this.lastPostDate = lastPostDate;
        this.modifiedDate = modifiedDate;
        this.forumReference = forumReference;
        this.posts = posts;
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

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getNumPosts() {
        return numPosts;
    }

    public void setNumPosts(Integer numPosts) {
        this.numPosts = numPosts;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Instant getLastPostDate() {
        return lastPostDate;
    }

    public void setLastPostDate(Instant lastPostDate) {
        this.lastPostDate = lastPostDate;
    }

    public Instant getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public ForumReferenceTo getForumReference() {
        return forumReference;
    }

    public void setForumReference(ForumReferenceTo forumReference) {
        this.forumReference = forumReference;
    }
}
