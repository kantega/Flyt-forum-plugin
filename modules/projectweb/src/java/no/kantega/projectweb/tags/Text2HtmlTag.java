package no.kantega.projectweb.tags;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.ExpressionEvaluationUtils;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;

import no.kantega.projectweb.permission.PermissionManager;
import no.kantega.projectweb.permission.Permissions;
import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.user.UserResolver;
import no.kantega.projectweb.user.UserProfileManager;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Sep 29, 2005
 * Time: 1:17:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Text2HtmlTag extends TagSupport {
    private String text;

    public int doStartTag() throws JspException {
        if(text != null) {
            String evaluatedText = ExpressionEvaluationUtils.evaluateString("text", text, pageContext);

            String htmlText = HtmlUtils.htmlEscape(evaluatedText).replaceAll("\n", "<br>");


            try {
                pageContext.getOut().write(htmlText);
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
        return SKIP_BODY;
    }


    public void setText(String text) {
        this.text = text;
    }
}
