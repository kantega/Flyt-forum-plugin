package no.kantega.forum.permission;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;

import no.kantega.forum.model.Post;


public class TestPermissionManager implements PermissionManager {

    private Logger log = Logger.getLogger(getClass());

    private Field[] permissionFields = Permissions.class.getFields();

    public boolean hasPermission(String user, long permission, Object object) {
        boolean has = getPermission(user, permission, object);

        String o = object == null ? "null" : object.toString();
        log.debug((has ? "Accepted" : "Rejected") +" permission " + getPermission(permission) +" for user " + (user == null ? "null" : user) +" on object " +o);
        return has;

    }

    private boolean getPermission(String user, long permission, Object object) {
        // Bjorsnos er administrator
        if("bjorsnos".equals(user)) {
            return true;
        }

        // Alle kan poste
        if(permission == Permissions.POST_IN_THREAD) {
            return true;
        }
        // Alle kan lage nye tråder
        if(permission == Permissions.EDIT_THREAD && object == null) {
            return true;
        }

        // Folk kan redigere egne poster
        if(permission == Permissions.EDIT_POST && object instanceof Post) {
            return user != null && user.equals(((Post)object).getOwner());
        }


        return false;
    }

    private String getPermission(long value) {
        try {
            for (int i = 0; i < permissionFields.length; i++) {
                Field permissionField = permissionFields[i];
                if(permissionField.getLong(null) == value) {
                    return permissionField.getName();
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return "Unknown field " + value;
    }

}
