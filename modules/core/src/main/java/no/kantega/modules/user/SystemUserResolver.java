package no.kantega.modules.user;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Nov 2, 2005
 * Time: 3:53:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class SystemUserResolver implements UserResolver {
    public ResolvedUser resolveUser(HttpServletRequest request) {
        ResolvedUser user = new ResolvedUser();
        user.setUsername(System.getProperty("user.name"));
        user.setRoles(new String[0]);
        return user;
    }
}
