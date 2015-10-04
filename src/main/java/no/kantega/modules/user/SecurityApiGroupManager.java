package no.kantega.modules.user;

import no.kantega.security.api.common.SystemException;
import no.kantega.security.api.role.Role;
import no.kantega.security.api.role.RoleManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SecurityApiGroupManager implements GroupManager {
    private RoleManager roleManager;

    public List<Group> getAllGroups() {
        try {
            List<Group> groups = new ArrayList<>();
            Iterator<Role> roles = roleManager.getAllRoles();
            while (roles.hasNext()) {
                Role role = roles.next();
                final String groupId = role.getDomain() + ":" + role.getId();
                final String groupName = role.getName();

                groups.add(new AksessGroup(groupId, groupName));
            }

            return groups;

        } catch (SystemException e) {
            return Collections.emptyList();
        }
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }
}
