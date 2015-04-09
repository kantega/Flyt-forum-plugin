package no.kantega.modules.user;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.base.algorithm.LRUCache;
import no.kantega.publishing.common.Aksess;
import no.kantega.publishing.security.data.Role;
import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AksessGroupResolver implements GroupResolver {
    private Cache cache = new Cache(true, false, false, false, LRUCache.class.getName(), 100);

    private Logger log = Logger.getLogger(getClass());

    public boolean isInGroup(String user, String group) {
        if (group.endsWith(Aksess.getEveryoneRole())) {
            return true;
        }

        if (user == null) {
            return false;
        }

        Map<String, Role> roles;

        try {
            // Cache for one hour
            roles = (Map<String, Role>) cache.getFromCache(user, 3600);
        } catch (NeedsRefreshException e) {
            try {
                SecurityRealm realm = SecurityRealmFactory.getInstance();
                List<Role> userRoles = realm.lookupRolesForUser(user);

                roles = new HashMap<>();
                for (Role r : userRoles) {
                    roles.put(r.getId(), r);
                }

                cache.putInCache(user, roles);
            } catch (Exception e1) {
                cache.cancelUpdate(user);
                log.error(e1.getMessage(), e1);
                return false;
            }

        }
        return roles.containsKey(group);
    }
}
