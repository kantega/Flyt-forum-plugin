package no.kantega.modules.user;

import no.kantega.publishing.security.SecuritySession;
import no.kantega.commons.exception.SystemException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import org.apache.log4j.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 5, 2005
 * Time: 12:40:43 PM
 * To change this template use File | Settings | File Templates.
 */
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

            List roles = new ArrayList();

            Map roleMap = session.getUser().getRoles();
            Iterator i = roleMap.keySet().iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                roles.add(key);
            }
            user.setRoles((String[]) roles.toArray(new String[0]));
            return user;
        }
    }

}
