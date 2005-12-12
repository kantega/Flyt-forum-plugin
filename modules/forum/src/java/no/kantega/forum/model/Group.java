package no.kantega.forum.model;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 02.des.2005
 * Time: 12:02:56
 * To change this template use File | Settings | File Templates.
 */
public class Group {
    private long id;
    private String name;
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