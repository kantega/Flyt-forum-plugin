package no.kantega.forum.permission;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: May 22, 2006
 * Time: 10:41:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class PermissionObject {
    private long permission;
    private Object object;

    public PermissionObject(long permission, Object object) {
        this.permission = permission;
        this.object = object;
    }

    public long getPermission() {
        return permission;
    }

    public Object getObject() {
        return object;
    }
}
