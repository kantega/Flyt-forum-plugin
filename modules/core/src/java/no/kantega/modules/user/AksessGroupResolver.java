package no.kantega.modules.user;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.base.algorithm.LRUCache;
import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;
import org.apache.log4j.Logger;

import java.util.Map;


public class AksessGroupResolver implements GroupResolver {
    private Cache cache = new Cache(true, false, false, false, LRUCache.class.getName(), 100);

    private Logger log = Logger.getLogger(getClass());

    public boolean isInGroup(String user, String group) {

        Map roles;

        try {
            roles = (Map) cache.getFromCache(user, 5);
        } catch (NeedsRefreshException e) {
            try {
                SecurityRealm realm = SecurityRealmFactory.getInstance();
                roles = realm.lookupUser(user).getRoles();
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
