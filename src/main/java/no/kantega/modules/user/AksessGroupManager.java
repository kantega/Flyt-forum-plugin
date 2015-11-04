package no.kantega.modules.user;

import no.kantega.publishing.security.data.Role;
import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AksessGroupManager implements GroupManager {
    private Logger log = LoggerFactory.getLogger(AksessGroupManager.class);

    public List<Group> getAllGroups() {
        try {
            SecurityRealm realm = SecurityRealmFactory.getInstance();

            List<Role> roles = realm.getAllRoles();
            List<Group> groups = new ArrayList<>(roles.size());

            for (Role role : roles) {

                final String groupId = role.getId();
                final String groupName = role.getName();

                groups.add(new AksessGroup(groupId, groupName));
            }

            return groups;

        } catch (Exception e) {
            log.error("Error in getAllGroups", e);
            return Collections.emptyList();
        }
    }

}
