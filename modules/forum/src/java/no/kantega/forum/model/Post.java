package no.kantega.forum.model;

import java.util.Date;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 02.des.2005
 * Time: 14:45:36
 * To change this template use File | Settings | File Templates.
 */
public class Post {
    private long Id;
    private ForumThread Thread;
    private long ReplyToId;
    private String Subject;
    private String Body;
    private User Owner;
    private Date PostDate;
    private Set Attachments;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        this.Id = id;
    }

    public long getReplyToId() {
        return ReplyToId;
    }

    public void setReplyToId(long replyToId) {
        ReplyToId = replyToId;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public Date getPostDate() {
        return PostDate;
    }

    public void setPostDate(Date postDate) {
        PostDate = postDate;
    }

    public Set getAttachments() {
        return Attachments;
    }

    public void setAttachments(Set attachments) {
        Attachments = attachments;
    }

    public ForumThread getThread() {
        return Thread;
    }

    public void setThread(ForumThread thread) {
        this.Thread = thread;
    }

    public User getOwner() {
        return Owner;
    }

    public void setOwner(User owner) {
        Owner = owner;
    }
}
