package no.kantega.projectweb.user;

import no.kantega.publishing.security.SecuritySession;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 5, 2005
 * Time: 12:40:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class AksessUserResolver implements UserResolver {
    public ResolvedUser resolveUser(HttpServletRequest request) {
        SecuritySession session = (SecuritySession) request.getSession().getAttribute("aksess.securitySession");

        if(session == null) {
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
