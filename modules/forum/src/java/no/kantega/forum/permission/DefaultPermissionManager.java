package no.kantega.forum.permission;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Arrays;

import no.kantega.forum.model.Post;
import no.kantega.modules.user.GroupResolver;


public class DefaultPermissionManager implements PermissionManager {

    private Logger log = Logger.getLogger(getClass());

    private Field[] permissionFields = Permissions.class.getFields();

    private GroupResolver groupResolver;

    private List administratorGroups;

    public boolean hasPermission(String user, long permission, Object object) {
        boolean has = getPermission(user, permission, object);

        String o = object == null ? "null" : object.toString();
        log.debug((has ? "Accepted" : "Rejected") +" permission " + getPermission(permission) +" for user " + (user == null ? "null" : user) +" on object " +o);
        return has;

    }

    private boolean getPermission(String user, long permission, Object object) {

        // Sjekk om bruker er forumadministrator
        if(user != null && !user.trim().equals("")) {
            for (int i = 0; i < administratorGroups.size(); i++) {
                String group = (String) administratorGroups.get(i);
                if(groupResolver.isInGroup(user, group)) {
                    return true;
                }
            }
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

    public void setGroupResolver(GroupResolver groupResolver) {
        this.groupResolver = groupResolver;
    }

    public void setAdministratorGroups(String administratorGroups) {
        this.administratorGroups = Arrays.asList(administratorGroups.split(","));
    }
}
