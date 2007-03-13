package no.kantega.modules.tags;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;
import javax.servlet.http.HttpServletRequest;

import no.kantega.modules.user.UserResolver;
import no.kantega.modules.user.UserProfileManager;
import no.kantega.modules.user.UserProfile;
import no.kantega.modules.user.ResolvedUser;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: May 24, 2006
 * Time: 2:11:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserProfileTag extends ConditionalTagSupport {

    private String user = null;

    private String var = null;

    protected boolean condition() throws JspTagException {
        try {
            WebApplicationContext context = (WebApplicationContext) pageContext.getRequest().getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            UserResolver resolver = (UserResolver) context.getBean("userResolver");
            UserProfileManager manager = (UserProfileManager) context.getBean("userProfileManager");
            if (user == null) {
                ResolvedUser resolvedUser = resolver.resolveUser((HttpServletRequest) pageContext.getRequest());
                if(resolvedUser != null) {
                    user = resolvedUser.getUsername();
                }
            }
            String userString = ExpressionEvaluationUtils.evaluateString("user", user, pageContext);

            if (userString != null && !userString.trim().equals("")) {
                UserProfile profile = manager.getUserProfile(userString);
                pageContext.setAttribute(var, profile);
            }

            user = null;
            var = null;
            return true;
        } catch (JspException e) {
            throw new JspTagException(e.getMessage());
        }
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
