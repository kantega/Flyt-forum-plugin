package no.kantega.forum.permission;


public class TestPermissionManager implements PermissionManager {

    public boolean hasPermission(String user, long permission, Object object) {
        return "bjorsnos".equals(user);
    }

}
