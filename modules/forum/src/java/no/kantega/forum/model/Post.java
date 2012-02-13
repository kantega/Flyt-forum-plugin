package no.kantega.forum.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 02.des.2005
 * Time: 14:45:36
 * To change this template use File | Settings | File Templates.
 */
public class Post implements Comparable<Post>{
    private long id;
    private ForumThread thread;
    private long replyToId;
    private String subject;
    private String body;
    private String owner;
    private Date postDate;
    private Set attachments;
    private String author;
    private boolean isApproved;

    private Float ratingScore = 0f;
    private Integer numberOfRatings = 0;

    private List<Post> replyPosts;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(long replyToId) {
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

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Set getAttachments() {
        return attachments;
    }

    public void setAttachments(Set attachments) {
        this.attachments = attachments;
    }

    public ForumThread getThread() {
        return thread;
    }

    public void setThread(ForumThread thread) {
        this.thread = thread;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
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

    public List<Post> getReplyPosts() {
        return replyPosts;
    }

    public void setReplyPosts(List<Post> replyPosts) {
        this.replyPosts = replyPosts;
    }

    public boolean isContentComment() {
        if (this.getThread() == null) {
            return false;
        }

        return this.getThread().isContentComment();
    }

    public int compareTo(Post o) {
        return getPostDate().compareTo(o.postDate);
    }
}
