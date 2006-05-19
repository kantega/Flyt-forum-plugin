package no.kantega.projectweb.tags;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

import no.kantega.projectweb.permission.PermissionManager;
import no.kantega.projectweb.permission.GlobalPermissions;
import no.kantega.modules.user.UserResolver;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Sep 29, 2005
 * Time: 1:17:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class HasGlobalPermissionTag extends ConditionalTagSupport {
    private String user;
    private String permission;

    public boolean condition() {

        try {
            WebApplicationContext context = (WebApplicationContext) pageContext.getRequest().getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            System.out.println("Context is: " + context);
            PermissionManager manager = (PermissionManager) context.getBean("permissionManager");

            Class c = GlobalPermissions.class;
            Field field = c.getField(permission);
            long permissionId = field.getLong(null);

            if (user == null) {
                UserResolver userResolver = (UserResolver) context.getBean("userResolver");
                user = userResolver.resolveUser((HttpServletRequest) pageContext.getRequest()).getUsername();
            }

            return manager.hasGlobalPermission(user, permissionId);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void setUser(String user) {
        this.user = user;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int doAfterBody() throws JspException {
        return SKIP_BODY;
    }
}
