package no.kantega.forum.jaxrs.bol;

import java.time.Instant;

/**
 * @author Kristian Myrhaug
 * @since 2015-08-05
 */
public class PostBo {

    private Long id;
    private Long threadId;
    private Long replyToId;
    private String author;
    private String subject;
    private String body;
    private Instant postDate;
    private String owner;
    private Boolean approved;
    private Float ratingScore;
    private Integer numberOfRatings;
    private Instant modifiedDate;
    private String embed;

    public PostBo() {
    }

    public PostBo(Long id, Long threadId, Long replyToId, String author, String subject, String body, Instant postDate, String owner, Boolean approved, Float ratingScore, Integer numberOfRatings, Instant modifiedDate, String embed) {
        this.id = id;
        this.threadId = threadId;
        this.replyToId = replyToId;
        this.author = author;
        this.subject = subject;
        this.body = body;
        this.postDate = postDate;
        this.owner = owner;
        this.approved = approved;
        this.ratingScore = ratingScore;
        this.numberOfRatings = numberOfRatings;
        this.modifiedDate = modifiedDate;
        this.embed = embed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public Long getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(Long replyToId) {
        this.replyToId = replyToId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public Instant getPostDate() {
        return postDate;
    }

    public void setPostDate(Instant postDate) {
        this.postDate = postDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public String getEmbed() {
        return embed;
    }

    public void setEmbed(String embed) {
        this.embed = embed;
    }
}
