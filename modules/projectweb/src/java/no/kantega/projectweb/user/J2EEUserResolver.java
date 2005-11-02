package no.kantega.projectweb.user;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 24.sep.2005
 * Time: 15:04:22
 * To change this template use File | Settings | File Templates.
 */
public class J2EEUserResolver implements UserResolver {
    public ResolvedUser resolveUser(HttpServletRequest request) {
        ResolvedUser user = new ResolvedUser();
        user.setUsername(request.getUserPrincipal().getName());
        user.setRoles(new String[0]);
        return user;
    }
}
