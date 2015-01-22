package no.kantega.forum.control;

import no.kantega.modules.user.UserResolver;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.forum.permission.Permissions;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Anders Skar, Kantega AS
 * Date: Mar 21, 2007
 * Time: 12:17:12 PM
 */
public abstract class AbstractForumViewController implements Controller {
    protected UserResolver userResolver;
    protected PermissionManager permissionManager;

    public boolean isAuthorized(HttpServletRequest request, Object object) {
        String username = null;
        ResolvedUser user = userResolver.resolveUser(request);
        if (user != null) {
            username = user.getUsername();
        }
        return permissionManager.hasPermission(username, Permissions.VIEW, object);
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }
}
