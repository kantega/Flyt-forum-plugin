package no.kantega.projectweb.model;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 7, 2005
 * Time: 11:58:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class GroupMembership {
    private long id;
    private Group group;
    private String user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
