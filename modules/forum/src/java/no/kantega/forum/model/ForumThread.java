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
    private Forum Forum;
    private Date CreatedDate;
    private int NumPosts;
    private Set Posts;
    private Set Groups;
    private User Owner;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Forum getForum() {
        return Forum;
    }

    public void setForum(Forum forum) {
        Forum = forum;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        CreatedDate = createdDate;
    }

    public Set getPosts() {
        return Posts;
    }

    public void setPosts(Set posts) {
        Posts = posts;
    }

    public int getNumPosts() {
        return NumPosts;
    }

    public void setNumPosts(int numPosts) {
        NumPosts = numPosts;
    }

    public Set getGroups() {
        return Groups;
    }

    public void setGroups(Set groups) {
        Groups = groups;
    }

    public User getOwner() {
        return Owner;
    }

    public void setOwner(User owner) {
        Owner = owner;
    }
}

