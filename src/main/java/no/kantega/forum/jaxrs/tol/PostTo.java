package no.kantega.forum.jaxrs.tol;

import no.kantega.forum.jaxrs.bol.PostBo;
import no.kantega.forum.jaxrs.jaxb.InstantXmlAdapter;
import no.kantega.forum.model.Attachment;
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
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@XmlType(name = "Post")
@XmlRootElement(name = "post")
@XmlAccessorType(XmlAccessType.NONE)
public class PostTo {

    @XmlElement(name = "id")
    private Long id;

    @XmlElement(name = "replyToId")
    private Long replyToId;

    @XmlElement(name = "subject")
    private String subject;

    @XmlElement(name = "body")
    private String body;

    @XmlElement(name = "owner")
    private String owner;

    @XmlElement(name = "postDate")
    @XmlJavaTypeAdapter(InstantXmlAdapter.class)
    private Instant postDate;

    @XmlElement(name = "author")
    private String author;

    @XmlElement(name = "approved")
    private Boolean approved;

    @XmlElement(name = "ratingScore")
    private Float ratingScore;

    @XmlElement(name = "numberOfRatings")
    private Integer numberOfRatings;

    @XmlElement(name = "modifiedDate")
    @XmlJavaTypeAdapter(InstantXmlAdapter.class)
    private Instant modifiedDate;

    @XmlElement(name = "threadReference")
    private ResourceReferenceTo threadReference;

    @XmlElementWrapper(name = "likes")
    @XmlElement(name = "like")
    private List<LikeTo> likes;

    @XmlElement(name = "embed")
    private String embed;

    @XmlElementWrapper(name = "attachments")
    @XmlElement(name = "attachment")
    private List<AttachmentTo> attachments;

    @XmlElementWrapper(name = "actions")
    @XmlElement(name = "action")
    private List<ResourceReferenceTo> actions;

    /*
    @XmlElement(name = "")
    private Set<Attachment> attachments;
    */

    public PostTo() {
    }

    public PostTo(PostBo postBo, ResourceReferenceTo threadReference, List<LikeTo> likes, List<AttachmentTo> attachments, List<ResourceReferenceTo> actions){
        this.id = postBo.getId();
        this.replyToId = postBo.getReplyToId();
        this.subject = postBo.getSubject();
        this.body = postBo.getBody();
        this.owner = postBo.getOwner();
        this.postDate = postBo.getPostDate() != null ? new Instant(postBo.getPostDate().toEpochMilli()) : null;
        this.author = postBo.getAuthor();
        this.approved = postBo.getApproved();
        this.ratingScore = postBo.getRatingScore();
        this.numberOfRatings = postBo.getNumberOfRatings();
        this.modifiedDate = postBo.getModifiedDate() != null ? new Instant(postBo.getModifiedDate().toEpochMilli()) : null;
        this.threadReference = threadReference;
        this.likes = likes;
        this.embed = postBo.getEmbed();
        this.attachments = attachments;
        this.actions = actions;
    }

    public PostTo(Post postBo, ResourceReferenceTo threadReference, List<LikeTo> likes, List<AttachmentTo> attachments, List<ResourceReferenceTo> actions) {
        this.id = postBo.getId();
        this.replyToId = postBo.getReplyToId();
        this.subject = postBo.getSubject();
        this.body = postBo.getBody();
        this.owner = postBo.getOwner();
        this.postDate = postBo.getPostDate() != null ? new Instant(postBo.getPostDate().getTime()) : null;
        this.author = postBo.getAuthor();
        this.approved = postBo.isApproved();
        this.ratingScore = postBo.getRatingScore();
        this.numberOfRatings = postBo.getNumberOfRatings();
        this.modifiedDate = postBo.getModifiedDate() != null ? new Instant(postBo.getModifiedDate().getTime()) : null;
        this.threadReference = threadReference;
        this.likes = likes;
        this.embed = postBo.getEmbed();
        this.attachments = attachments;
        this.actions = actions;
    }

    public PostTo(Long id, Long replyToId, String subject, String body, String owner, Instant postDate, String author, Boolean approved, Float ratingScore, Integer numberOfRatings, Instant modifiedDate, ResourceReferenceTo threadReference, List<LikeTo> likes, String embed, List<AttachmentTo> attachments, List<ResourceReferenceTo> actions) {
        this.id = id;
        this.replyToId = replyToId;
        this.subject = subject;
        this.body = body;
        this.owner = owner;
        this.postDate = postDate;
        this.author = author;
        this.approved = approved;
        this.ratingScore = ratingScore;
        this.numberOfRatings = numberOfRatings;
        this.modifiedDate = modifiedDate;
        this.threadReference = threadReference;
        this.likes = likes;
        this.embed = embed;
        this.attachments = attachments;
        this.actions = actions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(Long replyToId) {
        this.replyToId = replyToId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Instant getPostDate() {
        return postDate;
    }

    public void setPostDate(Instant postDate) {
        this.postDate = postDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Float getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(Float ratingScore) {
        this.ratingScore = ratingScore;
    }

    public Integer getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(Integer numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public Instant getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public ResourceReferenceTo getThreadReference() {
        return threadReference;
    }

    public void setThreadReference(ResourceReferenceTo threadReference) {
        this.threadReference = threadReference;
    }

    public List<ResourceReferenceTo> getActions() {
        return actions;
    }

    public void setActions(List<ResourceReferenceTo> actions) {
        this.actions = actions;
    }

    public List<AttachmentTo> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentTo> attachments) {
        this.attachments = attachments;
    }

    public List<LikeTo> getLikes() {
        return likes;
    }

    public void setLikes(List<LikeTo> likes) {
        this.likes = likes;
    }
}
