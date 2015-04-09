package no.kantega.modules.user;

import javax.servlet.http.HttpServletRequest;

/**
 * Resolving user from request.getUserPrincipal()
 */
public class J2EEUserResolver implements UserResolver {
    public ResolvedUser resolveUser(HttpServletRequest request) {
        ResolvedUser user = new ResolvedUser();
        user.setUsername(request.getUserPrincipal().getName());
        user.setRoles(new String[0]);
        return user;
    }
}
