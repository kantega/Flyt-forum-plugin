package no.kantega.projectweb.tags;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

import no.kantega.projectweb.permission.PermissionManager;
import no.kantega.projectweb.permission.Permissions;
import no.kantega.projectweb.model.Project;
import no.kantega.modules.user.UserResolver;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Sep 29, 2005
 * Time: 1:17:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class HasPermissionTag extends ConditionalTagSupport {
    private Project project;
    private String user;
    private String permission;

    public boolean condition() {

        try {
            WebApplicationContext context = (WebApplicationContext) pageContext.getRequest().getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            PermissionManager manager = (PermissionManager) context.getBean("permissionManager");

            Class c = Permissions.class;
            Field field = c.getField(permission);
            long permissionId = field.getLong(null);

            if (user == null) {
                UserResolver userResolver = (UserResolver) context.getBean("userResolver");
                user = userResolver.resolveUser((HttpServletRequest) pageContext.getRequest()).getUsername();
            }

            boolean hasP = manager.hasPermission(user, permissionId, project);
            user = null;
            return hasP;


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

    public void setProject(Project project) {
        this.project = project;
    }

    public int doAfterBody() throws JspException {
        return SKIP_BODY;
    }
}
