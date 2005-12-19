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
    private String Name;
    private String Description;
    private int NumForums;
    private Date CreatedDate;
    private Set Groups;
    private User Owner;
    private Set Forums;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getNumForums() {
        return NumForums;
    }

    public void setNumForums(int numForums) {
        NumForums = numForums;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        CreatedDate = createdDate;
    }

    public Set getForums() {
        return Forums;
    }

    public void setForums(Set forums) {
        Forums = forums;
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
