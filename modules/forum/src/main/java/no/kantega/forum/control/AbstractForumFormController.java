package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.permission.PermissionManager;
import no.kantega.forum.permission.PermissionObject;
import no.kantega.modules.user.UserResolver;
import no.kantega.modules.user.ResolvedUser;

public class AbstractForumFormController extends SimpleFormController {


    protected PermissionManager permissionManager;
    protected UserResolver userResolver;

    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        return new PermissionObject[0];
    }


    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        if(!assertPermissions(request)) {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } else {
            return super.handleRequestInternal(request, httpServletResponse);
        }
    }

    protected boolean assertPermissions(HttpServletRequest request) {

        ResolvedUser user = userResolver.resolveUser(request);
        String userName = null;
        if(user != null) {
            userName = user.getUsername();
        }
        PermissionObject[] permissions = getRequiredPermissions(request);
        if (permissions != null) {
            for (PermissionObject permissionObject : permissions) {
                if (!permissionManager.hasPermission(userName, permissionObject.getPermission(), permissionObject.getObject())) {
                    return false;
                }
            }
        }
        return true;
    }

    public PermissionObject[] permissions(long permission) {
        return permissions(permission, null);
    }

    public PermissionObject[] permissions(long permission, Object object) {
        return new PermissionObject[] {new PermissionObject(permission, object)};
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }
}
