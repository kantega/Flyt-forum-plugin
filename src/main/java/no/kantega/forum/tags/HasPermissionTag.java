package no.kantega.forum.tags;

import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;

public class HasPermissionTag extends ConditionalTagSupport {
    private final Logger log = LoggerFactory.getLogger(HasPermissionTag.class);
    private Object object;
    private String user;
    private Permission permission;

    private PermissionManager manager;
    private UserResolver userResolver;

    public boolean condition() {

        try {
            if (user == null) {
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

    @Override
    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext());
        manager = context.getBean("forumPermissionManager", PermissionManager.class);
        userResolver = context.getBean("userResolver", UserResolver.class);
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
