package no.kantega.modules.user;

import no.kantega.security.api.role.RoleManager;
import no.kantega.security.api.identity.Identity;
import no.kantega.security.api.common.SystemException;
import no.kantega.publishing.common.Aksess;

/**
 * User: Anders Skar, Kantega AS
 * Date: Jan 27, 2007
 * Time: 10:34:45 AM
 */
public class SecurityApiGroupResolver implements GroupResolver {
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
            // TODO: Caching
            return roleManager.userHasRole(identity, group);
        } catch (SystemException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return false;
    }
}
