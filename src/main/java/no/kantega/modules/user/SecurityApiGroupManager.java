package no.kantega.modules.user;

import no.kantega.security.api.common.SystemException;
import no.kantega.security.api.role.Role;
import no.kantega.security.api.role.RoleManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Anders Skar, Kantega AS
 * Date: Mar 21, 2007
 * Time: 9:19:39 AM
 */
public class SecurityApiGroupManager implements GroupManager {
    private RoleManager roleManager;

    public Group[] getAllGroups() {
        try {
            List groups = new ArrayList();
            Iterator roles = roleManager.getAllRoles();
            while (roles.hasNext()) {
                Role role = (Role)roles.next();
                final String groupId = role.getDomain() + ":" + role.getId();
                final String groupName = role.getName();

                Group group = new Group() {
                    public String getId() {
                        return groupId;
                    }

                    public String getName() {
                        return groupName;
                    }
                };
                groups.add(group);
            }
            
            return (Group[]) groups.toArray(new Group[0]);

        } catch (SystemException e) {
            return new Group[0];
        }
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }
}
