package no.kantega.modules.user;

import no.kantega.publishing.security.SecuritySession;
import no.kantega.commons.exception.SystemException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import no.kantega.publishing.security.data.Role;
import org.apache.log4j.Logger;

public class AksessUserResolver implements UserResolver {
    private Logger log = Logger.getLogger(AksessUserResolver.class);

    public ResolvedUser resolveUser(HttpServletRequest request) {

        SecuritySession session = null;
        try {
            session = SecuritySession.getInstance(request);
        } catch (SystemException e) {
            log.error(e);
        }

        if(session == null || session.getUser() == null) {
            return null;
        } else {
            ResolvedUser user = new ResolvedUser();
            user.setUsername(session.getUser().getId());

            Map<String, Role> roleMap = session.getUser().getRoles();
            List<String> roles = new ArrayList<>(roleMap.keySet());

            user.setRoles(roles.toArray(new String[roles.size()]));
            return user;
        }
    }

}
