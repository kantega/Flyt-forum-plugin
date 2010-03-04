package no.kantega.forum.model;

import java.util.Date;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 02.des.2005
 * Time: 11:26:41
 * To change this template use File | Settings | File Templates.
 */
public class ForumThread {
    private long id;
    private String name;
    private String description;
    private Forum forum;
    private Date createdDate;
    private int numPosts;
    private Post lastPost;
    private Set posts;
    private String owner;
    private int contentId;
    private boolean isApproved;
    private int numNewPosts = 0;
    private Set topics;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Set getPosts() {
        return posts;
    }

    public void setPosts(Set posts) {
        this.posts = posts;
    }

    public int getNumPosts() {
        return numPosts;
    }

    public void setNumPosts(int numPosts) {
        this.numPosts = numPosts;
    }

    public int getNumNewPosts() {
        return numNewPosts;
    }

    public void setNumNewPosts(int numNewPosts) {
        this.numNewPosts = numNewPosts;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public Post getLastPost() {
        return lastPost;
    }

    public void setLastPost(Post lastPost) {
        this.lastPost = lastPost;
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

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public Set getTopics() {
        return topics;
    }

    public void setTopics(Set topics) {
        this.topics = topics;
    }
}

