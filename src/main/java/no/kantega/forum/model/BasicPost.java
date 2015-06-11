package no.kantega.forum.model;

import java.util.Date;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-11
 */
public class BasicPost {

    private long id;
    private long replyToId;
    private String subject;
    private String body;
    private String owner;
    private Date postDate;
    private String author;
    private boolean isApproved;
    private Float ratingScore = 0f;
    private Integer numberOfRatings = 0;
    private Date modifiedDate;

    public BasicPost() {
    }

    public BasicPost(Post post) {
        this.id = post.getId();
        this.replyToId = post.getReplyToId();
        this.subject = post.getSubject();
        this.body = post.getBody();
        this.owner = post.getOwner();
        this.postDate = post.getPostDate();
        this.author = post.getAuthor();
        this.isApproved = post.isApproved();
        this.ratingScore = post.getRatingScore();
        this.numberOfRatings = post.getNumberOfRatings();
        this.modifiedDate = post.getModifiedDate();
    }

    public BasicPost(long id, long replyToId, String subject, String body, String owner, Date postDate, String author, boolean isApproved, Float ratingScore, Integer numberOfRatings, Date modifiedDate) {
        this.id = id;
        this.replyToId = replyToId;
        this.subject = subject;
        this.body = body;
        this.owner = owner;
        this.postDate = postDate;
        this.author = author;
        this.isApproved = isApproved;
        this.ratingScore = ratingScore;
        this.numberOfRatings = numberOfRatings;
        this.modifiedDate = modifiedDate;
    }

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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
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

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
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

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
