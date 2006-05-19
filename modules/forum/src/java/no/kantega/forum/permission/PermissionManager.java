package no.kantega.forum.permission;

public interface PermissionManager {
    public boolean hasPermission(String user, int permission, Object object);
}
