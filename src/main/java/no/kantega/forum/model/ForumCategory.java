package no.kantega.forum.model;

import java.util.Date;
import java.util.Set;

public class ForumCategory {
    private long id;
    private String name;
    private String description;
    private int numForums;
    private Date createdDate;
    private String owner;
    private Set<Forum> forums;

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

    public Set<Forum> getForums() {
        return forums;
    }

    public void setForums(Set<Forum> forums) {
        this.forums = forums;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String toString() {
        return getClass().getName() +": " +id +", " + name;
    }
}
