package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.InitializingBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.permission.PermissionManager;
import no.kantega.forum.permission.PermissionObject;
import no.kantega.modules.user.UserResolver;
import no.kantega.modules.user.ResolvedUser;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: May 22, 2006
 * Time: 9:47:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class AbstractForumFormController extends SimpleFormController implements InitializingBean {


    private PermissionManager permissionManager;
    private UserResolver userResolver;

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

        for (int i = 0; i < permissions.length; i++) {
            PermissionObject permissionObject = permissions[i];

            if(!permissionManager.hasPermission(userName, permissionObject.getPermission(), permissionObject.getObject())) {
                return false;
            }
        }
        return true;
    }


    public void afterPropertiesSet() throws Exception {
        permissionManager = (PermissionManager) getApplicationContext().getBean("permissionManager", PermissionManager.class);
        userResolver = (UserResolver) getApplicationContext().getBean("userResolver", UserResolver.class);
    }

    public PermissionObject[] permissions(long permission) {
        return permissions(permission, null);
    }

    public PermissionObject[] permissions(long permission, Object object) {
        return new PermissionObject[] {new PermissionObject(permission, object)};
    }
}