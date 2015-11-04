package no.kantega.modules.user;

import no.kantega.publishing.common.Aksess;
import no.kantega.publishing.security.data.Role;
import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;


public class AksessGroupResolver implements GroupResolver {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Cacheable("ForumGroupCache")
    public boolean isInGroup(String user, String group) {
        log.debug("isInGroup {} {}", user, group);
        if (group.endsWith(Aksess.getEveryoneRole())) {
            return true;
        }

        if (user == null) {
            return false;
        }

        SecurityRealm realm = SecurityRealmFactory.getInstance();
        List<Role> userRoles = realm.lookupRolesForUser(user);

        return userRoles.stream()
                .map(Role::getId)
                .anyMatch(roleid -> roleid.equals(group));
    }
}
