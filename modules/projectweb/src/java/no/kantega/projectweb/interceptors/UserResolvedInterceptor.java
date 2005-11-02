package no.kantega.projectweb.interceptors;

import no.kantega.projectweb.user.UserResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 5, 2005
 * Time: 12:54:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserResolvedInterceptor extends HandlerInterceptorAdapter {
    private UserResolver userResolver;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        if(userResolver.resolveUser(request) == null) {
            response.setContentType("text/html");
            response.getWriter().println("User could not be resolved. Is your user resolver properly set up?");
            return false;
        }
        return true;
    }


    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }
}
