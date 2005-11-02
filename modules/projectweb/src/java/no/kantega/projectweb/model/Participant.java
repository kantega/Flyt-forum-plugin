package no.kantega.projectweb.model;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 23.sep.2005
 * Time: 15:15:36
 * To change this template use File | Settings | File Templates.
 */
public class Participant {
    private long id;
    private String user;
    private Project project;
    private Set roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Set getRoles() {
        return roles;
    }

    public void setRoles(Set roles) {
        this.roles = roles;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
