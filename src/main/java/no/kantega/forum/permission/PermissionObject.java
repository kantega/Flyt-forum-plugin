package no.kantega.forum.permission;

public class PermissionObject {
    private Permission permission;
    private Object object;

    public PermissionObject(Permission permission, Object object) {
        this.permission = permission;
        this.object = object;
    }

    public Permission getPermission() {
        return permission;
    }

    public Object getObject() {
        return object;
    }
}
