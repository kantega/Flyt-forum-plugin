package no.kantega.modules.user;

import no.kantega.publishing.common.Aksess;
import no.kantega.security.api.common.SystemException;
import no.kantega.security.api.identity.Identity;
import no.kantega.security.api.role.RoleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityApiGroupResolver implements GroupResolver {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private RoleManager roleManager;

    public boolean isInGroup(String user, String group) {
        if (group.endsWith(Aksess.getEveryoneRole())) {
            return true;
        }

        if (user == null) {
            return false;
        }

        Identity identity = SecurityApiHelper.createApiIdentity(user);
        try {
            return roleManager.userHasRole(identity, group);
        } catch (SystemException e) {
            log.error("Error checking role");
        }
        return false;
    }
}
