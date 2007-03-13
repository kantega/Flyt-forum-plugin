package no.kantega.projectweb.permission.scheme;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 24.sep.2005
 * Time: 23:08:01
 * To change this template use File | Settings | File Templates.
 */
public class PermissionEntry {
    private long id;
    private Set roles;
    private long permission;
    private PermissionScheme permissionScheme;

    public Set getRoles() {
        return roles;
    }

    public void setRoles(Set roles) {
        this.roles = roles;
    }

    public long getPermission() {
        return permission;
    }

    public void setPermission(long permission) {
        this.permission = permission;
    }

    public PermissionScheme getPermissionScheme() {
        return permissionScheme;
    }

    public void setPermissionScheme(PermissionScheme permissionScheme) {
        this.permissionScheme = permissionScheme;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
