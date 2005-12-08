package no.kantega.forum.model;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 02.des.2005
 * Time: 12:03:59
 * To change this template use File | Settings | File Templates.
 */
public class User {
    private int id;
    private String name;
    private Set role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set getRole() {
        return role;
    }

    public void setRole(Set role) {
        this.role = role;
    }
}
