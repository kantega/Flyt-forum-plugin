package no.kantega.forum.control;

import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractForumViewController implements Controller {
    protected UserResolver userResolver;
    protected PermissionManager permissionManager;

    public boolean isAuthorized(HttpServletRequest request, Object object) {
        String username = null;
        ResolvedUser user = userResolver.resolveUser(request);
        if (user != null) {
            username = user.getUsername();
        }
        return permissionManager.hasPermission(username, Permission.VIEW, object);
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }
}
