package no.kantega.forum.tags.wall;


import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class RenderWallScriptsAndCssTag extends SimpleTagSupport{

    public void doTag() throws JspException, IOException {
        try {
            PageContext pageContext = ((PageContext)getJspContext());
            pageContext.include("/WEB-INF/jsp/forum/wall/scripts-and-css.jsp");
        } catch (ServletException e) {
            throw new JspException(e);
        }
    }
}

