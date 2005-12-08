package no.kantega.forum.model;

import java.util.Date;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 02.des.2005
 * Time: 11:09:00
 * To change this template use File | Settings | File Templates.
 */
public class Forum {
    private int id;
    private ForumCategory ForumCategory;
    private String Name;
    private String Description;
    private int NumThreads;   // num threads in current Forum
    private int LastMessage;  // last message posted in Forum
    private Date CreatedDate; // Forum Created Date
    private Set Threads;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getLastMessage() {
        return LastMessage;
    }

    public void setLastMessage(int lastMessage) {
        LastMessage = lastMessage;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        CreatedDate = createdDate;
    }

    public Set getThreads() {
        return Threads;
    }

    public void setThreads(Set threads) {
        Threads = threads;
    }

    public int getNumThreads() {
        return NumThreads;
    }

    public void setNumThreads(int numThreads) {
        NumThreads = numThreads;
    }

    public ForumCategory getForumCategory() {
        return ForumCategory;
    }

    public void setForumCategory(ForumCategory forumCategory) {
        ForumCategory = forumCategory;
    }
}
