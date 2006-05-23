package no.kantega.forum.tags;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.jstl.core.ConditionalTagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

import no.kantega.forum.permission.PermissionManager;
import no.kantega.forum.permission.Permissions;
import no.kantega.modules.user.UserResolver;
import no.kantega.modules.user.ResolvedUser;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Sep 29, 2005
 * Time: 1:17:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class HasPermissionTag extends ConditionalTagSupport {
    private String object;
    private String user;
    private String permission;

    public boolean condition() {

        try {
            WebApplicationContext context = (WebApplicationContext) pageContext.getRequest().getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            PermissionManager manager = (PermissionManager) context.getBean("permissionManager");
            Object o  = null;

            if(object != null) {
                o = ExpressionEvaluationUtils.evaluate("object", object, Object.class, pageContext);
            }
            Class c = Permissions.class;
            Field field = c.getField(permission);
            long permissionId = field.getLong(null);

            if (user == null) {
                UserResolver userResolver = (UserResolver) context.getBean("userResolver");
                ResolvedUser resolvedUser = userResolver.resolveUser((HttpServletRequest) pageContext.getRequest());
                if(resolvedUser != null) {
                    user = resolvedUser.getUsername();
                }
            }

            return manager.hasPermission(user, permissionId, o);


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            user = null;
            object = null;
            permission = null;
        }
    }


    public void setUser(String user) {
        this.user = user;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public int doAfterBody() throws JspException {
        return SKIP_BODY;
    }
}
