package no.kantega.modules.user;

import no.kantega.security.api.identity.Identity;
import no.kantega.security.api.identity.DefaultIdentity;
import no.kantega.security.api.role.DefaultRole;
import no.kantega.security.api.role.Role;

/**
 * User: Anders Skar, Kantega AS
 * Date: Jan 27, 2007
 * Time: 10:46:35 AM
 */
public class SecurityApiHelper {
    public static Identity createApiIdentity(String aksessUserId) {
        String userId = "";
        String domain = "";
        if (aksessUserId.indexOf(":") == -1) {
            userId = aksessUserId;
            domain = null;
        } else {
            userId = aksessUserId.substring(aksessUserId.indexOf(":") + 1, aksessUserId.length());
            domain = aksessUserId.substring(0, aksessUserId.indexOf(":"));
        }

        DefaultIdentity identity = new DefaultIdentity();
        identity.setUserId(userId);
        identity.setDomain(domain);

        return identity;
    }

    public static no.kantega.security.api.role.Role createApiRole(String aksessRoleId) {
        String roleId = "";
        String domain = "";
        if (aksessRoleId.indexOf(":") == -1) {
            roleId = aksessRoleId;
            domain = null;
        } else {
            roleId = aksessRoleId.substring(aksessRoleId.indexOf(":") + 1, aksessRoleId.length());
            domain = aksessRoleId.substring(0, aksessRoleId.indexOf(":"));
        }

        DefaultRole role = new DefaultRole();
        role.setId(roleId);
        role.setDomain(domain);

        return role;
    }

    public static String createUserId(Identity identity) {
        return identity.getDomain() + ":" + identity.getUserId();
    }

    public static String createRoleId(Role r) {
        return r.getDomain() + ":" + r.getId();
    }

}
