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
    private int Id;
    private ForumThread Thread;
    private int ReplyToId;
    private String Subject;
    private String Body;
    private User user;
    private Date PostDate;
    private Set Attachments;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getReplyToId() {
        return ReplyToId;
    }

    public void setReplyToId(int replyToId) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
