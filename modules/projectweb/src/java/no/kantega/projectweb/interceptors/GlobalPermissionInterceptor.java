package no.kantega.projectweb.interceptors;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.projectweb.permission.PermissionManager;
import no.kantega.projectweb.user.UserResolver;
import no.kantega.projectweb.user.ResolvedUser;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 10, 2005
 * Time: 5:22:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class GlobalPermissionInterceptor extends HandlerInterceptorAdapter {
    private PermissionManager permissionManager;
    private UserResolver userResolver;
    private int permission;
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        ResolvedUser user = userResolver.resolveUser(httpServletRequest);
        if(user != null) {
            return permissionManager.hasGlobalPermission(user.getUsername(), permission);
        }
        return false;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
