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
public class MenuSelectionInterceptor extends HandlerInterceptorAdapter {
    private String selected;

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        httpServletRequest.setAttribute("selectedMenu", selected);
        return true;
    }


    public void setSelected(String selected) {
        this.selected = selected;
    }
}
