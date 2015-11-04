package no.kantega.modules.user;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * Resolving user from request.getUserPrincipal()
 */
public class J2EEUserResolver implements UserResolver {
    public ResolvedUser resolveUser(HttpServletRequest request) {
        ResolvedUser user = new ResolvedUser();
        user.setUsername(request.getUserPrincipal().getName());
        user.setRoles(Collections.<String>emptyList());
        return user;
    }
}
