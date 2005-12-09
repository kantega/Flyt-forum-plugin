package no.kantega.forum.model;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 09.des.2005
 * Time: 11:53:20
 * To change this template use File | Settings | File Templates.
 */
public class Role {
    private long id;
    private String name;
    private String description;
    private Set users;

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

    public Set getUsers() {
        return users;
    }

    public void setUsers(Set users) {
        this.users = users;
    }

    public boolean hasUser(User user) {
        if (this.users != null && this.users.contains(user)) {
            return true;
        }

        return false;
    }
}
