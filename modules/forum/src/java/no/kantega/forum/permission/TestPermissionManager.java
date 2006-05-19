package no.kantega.forum.permission;


public class TestPermissionManager implements PermissionManager {

    public boolean hasPermission(String user, int permission, Object object) {
        return user.equals("bjorsnos");
    }

}
