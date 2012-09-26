package no.kantega.modules.user;

import no.kantega.security.api.identity.IdentityResolver;
import no.kantega.security.api.identity.IdentificationFailedException;
import no.kantega.security.api.identity.AuthenticatedIdentity;
import no.kantega.security.api.profile.ProfileManager;
import no.kantega.security.api.profile.Profile;
import no.kantega.security.api.common.SystemException;
import no.kantega.security.api.role.RoleManager;
import no.kantega.security.api.role.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * User: Anders Skar, Kantega AS
 * Date: Jan 27, 2007
 * Time: 10:58:09 AM
 */
public class SecurityApiUserResolver implements UserResolver {
    private final String RESOLVED_USER_ATTR = "aksess.modules.securityapi-user";
    private Logger log = Logger.getLogger(SecurityApiUserResolver.class);

    private IdentityResolver identityResolver;
    private RoleManager profileManager;

    public ResolvedUser resolveUser(HttpServletRequest request) {
        HttpSession session = request.getSession();

        ResolvedUser user = null;
        try {
            user = (ResolvedUser)session.getAttribute(RESOLVED_USER_ATTR);

            AuthenticatedIdentity identity = identityResolver.getIdentity(request);
            if (identity != null && user == null) {
                user = new ResolvedUser();
                user.setUsername(SecurityApiHelper.createUserId(identity));

                Iterator roles = profileManager.getRolesForUser(identity);
                List roleList = new ArrayList();

                while (roles.hasNext()) {
                    Role r = (Role)roles.next();
                    roleList.add(SecurityApiHelper.createRoleId(r));
                }
                user.setRoles((String[]) roleList.toArray(new String[0]));

                session.setAttribute(RESOLVED_USER_ATTR, user);
            }

        } catch (IdentificationFailedException e) {
            log.error("", e);

        } catch (SystemException e) {
            log.error("", e);
        }
        return user;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setIdentityResolver(IdentityResolver identityResolver) {
        this.identityResolver = identityResolver;
    }

    public void setRoleManager(RoleManager profileManager) {
        this.profileManager = profileManager;
    }
}
