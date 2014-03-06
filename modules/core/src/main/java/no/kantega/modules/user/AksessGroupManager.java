package no.kantega.modules.user;

import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;
import no.kantega.publishing.security.data.Role;

import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * User: Anders Skar, Kantega AS
 * Date: Mar 21, 2007
 * Time: 9:19:13 AM
 */
public class AksessGroupManager implements GroupManager {
    private Logger log = Logger.getLogger(AksessGroupManager.class);

    public Group[] getAllGroups() {
        try {
            SecurityRealm realm = SecurityRealmFactory.getInstance();

            List groups = new ArrayList();
            List roles = realm.getAllRoles();

            for (int i = 0; i < roles.size(); i++) {
                Role role = (Role)roles.get(i);

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

            return (Group[]) groups.toArray(new Group[0]);

        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}
