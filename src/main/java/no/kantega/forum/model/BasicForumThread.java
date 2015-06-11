package no.kantega.forum.model;

import java.util.Date;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-11
 */
public class BasicForumThread {

    private long id;
    private String name;
    private String description;
    private Date createdDate;
    private int numPosts;
    private String owner;
    private int contentId;
    private boolean isApproved;
    private int numNewPosts = 0;
    private Date lastPostDate;
    private Date modifiedDate;

    public BasicForumThread() {
    }

    public BasicForumThread(ForumThread forumThread) {
        this.id = forumThread.getId();
        this.name = forumThread.getName();
        this.description = forumThread.getDescription();
        this.createdDate = forumThread.getCreatedDate();
        this.numPosts = forumThread.getNumPosts();
        this.owner = forumThread.getOwner();
        this.contentId = forumThread.getContentId();
        this.isApproved = forumThread.isApproved();
        this.numNewPosts = forumThread.getNumNewPosts();
        this.lastPostDate = forumThread.getLastPostDate();
        this.modifiedDate = forumThread.getModifiedDate();
    }

    public BasicForumThread(long id, String name, String description, Date createdDate, int numPosts, String owner, int contentId, boolean isApproved, int numNewPosts, Date lastPostDate, Date modifiedDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.numPosts = numPosts;
        this.owner = owner;
        this.contentId = contentId;
        this.isApproved = isApproved;
        this.numNewPosts = numNewPosts;
        this.lastPostDate = lastPostDate;
        this.modifiedDate = modifiedDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getNumPosts() {
        return numPosts;
    }

    public void setNumPosts(int numPosts) {
        this.numPosts = numPosts;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public int getNumNewPosts() {
        return numNewPosts;
    }

    public void setNumNewPosts(int numNewPosts) {
        this.numNewPosts = numNewPosts;
    }

    public Date getLastPostDate() {
        return lastPostDate;
    }

    public void setLastPostDate(Date lastPostDate) {
        this.lastPostDate = lastPostDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
