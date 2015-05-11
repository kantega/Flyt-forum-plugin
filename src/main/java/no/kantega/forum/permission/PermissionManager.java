package no.kantega.forum.permission;

public interface PermissionManager {
    boolean hasPermission(String user, Permission permission, Object object);
}
