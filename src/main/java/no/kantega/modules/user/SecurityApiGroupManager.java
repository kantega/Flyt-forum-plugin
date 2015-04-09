package no.kantega.modules.user;

import no.kantega.security.api.common.SystemException;
import no.kantega.security.api.role.Role;
import no.kantega.security.api.role.RoleManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SecurityApiGroupManager implements GroupManager {
    private RoleManager roleManager;

    public Group[] getAllGroups() {
        try {
            List<Group> groups = new ArrayList<>();
            Iterator<Role> roles = roleManager.getAllRoles();
            while (roles.hasNext()) {
                Role role = roles.next();
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

            return groups.toArray(new Group[groups.size()]);

        } catch (SystemException e) {
            return new Group[0];
        }
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }
}
