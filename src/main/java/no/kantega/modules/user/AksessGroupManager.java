package no.kantega.modules.user;

import no.kantega.publishing.security.data.Role;
import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AksessGroupManager implements GroupManager {
    private Logger log = Logger.getLogger(AksessGroupManager.class);

    public Group[] getAllGroups() {
        try {
            SecurityRealm realm = SecurityRealmFactory.getInstance();

            List<Group> groups = new ArrayList<>();
            List<Role> roles = realm.getAllRoles();

            for (Role role : roles) {

                final String groupId = role.getId();
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

        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}
