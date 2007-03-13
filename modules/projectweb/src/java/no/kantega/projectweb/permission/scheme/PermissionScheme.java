package no.kantega.projectweb.permission.scheme;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 24.sep.2005
 * Time: 22:55:40
 * To change this template use File | Settings | File Templates.
 */
public class PermissionScheme {
    private long id;
    private String name;
    private String description;
    private Set permissionEntries;

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

    public Set getPermissionEntries() {
        return permissionEntries;
    }

    public void setPermissionEntries(Set permissionEntries) {
        this.permissionEntries = permissionEntries;
    }
}
