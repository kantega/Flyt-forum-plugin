package no.kantega.projectweb.viewmodel;

import no.kantega.projectweb.model.ProjectRole;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 5, 2005
 * Time: 10:04:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class HasRole {
    private ProjectRole role;
    private boolean hasRole;

    public ProjectRole getRole() {
        return role;
    }

    public void setRole(ProjectRole role) {
        this.role = role;
    }

    public boolean isHasRole() {
        return hasRole;
    }

    public void setHasRole(boolean hasRole) {
        this.hasRole = hasRole;
    }
}
