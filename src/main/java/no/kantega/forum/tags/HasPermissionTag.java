package no.kantega.forum.tags;

import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;

public class HasPermissionTag extends ConditionalTagSupport {
    private final Logger log = LoggerFactory.getLogger(HasPermissionTag.class);
    private Object object;
    private String user;
    private Permission permission;

    public boolean condition() {

        try {
            WebApplicationContext context = (WebApplicationContext) pageContext.getRequest().getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            PermissionManager manager = context.getBean("forumPermissionManager", PermissionManager.class);

            if (user == null) {
                UserResolver userResolver = context.getBean("userResolver", UserResolver.class);
                ResolvedUser resolvedUser = userResolver.resolveUser((HttpServletRequest) pageContext.getRequest());
                if(resolvedUser != null) {
                    user = resolvedUser.getUsername();
                }
            }

            boolean hasP = manager.hasPermission(user, permission, object);
            user = null;

            return hasP;
        } catch (Exception e) {
            log.error("Woops", e);
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

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int doAfterBody() throws JspException {
        return SKIP_BODY;
    }
}
