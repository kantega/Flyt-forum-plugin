package no.kantega.forum.tags.wall;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class RenderWallScript extends SimpleTagSupport{

    public void doTag() throws JspException, IOException {
        PageContext pageContext = ((PageContext)getJspContext());
        try {
            pageContext.include("/WEB-INF/jsp/forum/wall/script.jsp");
        } catch (ServletException e) {
            throw new JspException(e);
        }
    }
}

