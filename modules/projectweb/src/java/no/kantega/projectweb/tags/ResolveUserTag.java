package no.kantega.projectweb.tags;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import no.kantega.projectweb.user.UserResolver;
import no.kantega.projectweb.user.UserProfileManager;
import no.kantega.projectweb.user.UserProfile;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Sep 29, 2005
 * Time: 1:17:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResolveUserTag extends TagSupport {
    private String user;

    private String info;
    public int doStartTag() throws JspException {

        WebApplicationContext context = (WebApplicationContext) pageContext.getRequest().getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        UserResolver resolver = (UserResolver) context.getBean("userResolver");
        UserProfileManager manager = (UserProfileManager)context.getBean("userProfileManager");
        if(user == null) {
            user = resolver.resolveUser((HttpServletRequest)pageContext.getRequest()).getUsername();
        }
        String userString = ExpressionEvaluationUtils.evaluateString("user", user, pageContext);

        if(userString == null || userString.trim().equals("")) {
            return SKIP_BODY;
        }

        UserProfile profile = manager.getUserProfile(userString);
        if(profile == null) {
            return SKIP_BODY;
        }

        String resolvedString;
        if(info != null && info.equals("email")) {
            resolvedString = manager.getUserProfile(userString).getEmail();
        } else {
            resolvedString = manager.getUserProfile(userString).getFullName();
        }

        try {
            pageContext.getOut().write(resolvedString);
        } catch (IOException e) {
            throw new JspException(e);
        }

        return SKIP_BODY;
    }


    public void setUser(String user) {
        this.user = user;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
