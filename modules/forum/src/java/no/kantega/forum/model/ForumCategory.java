package no.kantega.forum.model;

import java.util.Date;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 02.des.2005
 * Time: 12:25:29
 * To change this template use File | Settings | File Templates.
 */
public class ForumCategory {
    private long id;
    private String name;
    private String description;
    private int numForums;
    private Date createdDate;
    private Set groups;
    private User owner;
    private Set forums;

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

    public int getNumForums() {
        return numForums;
    }

    public void setNumForums(int numForums) {
        this.numForums = numForums;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Set getForums() {
        return forums;
    }

    public void setForums(Set forums) {
        this.forums = forums;
    }

    public Set getGroups() {
        return groups;
    }

    public void setGroups(Set groups) {
        this.groups = groups;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
