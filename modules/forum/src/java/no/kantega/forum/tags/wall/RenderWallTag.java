package no.kantega.forum.tags.wall;


import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class RenderWallTag extends SimpleTagSupport{

    private boolean sharebox = false;

    public void doTag() throws JspException, IOException {
        try {
            PageContext pageContext = ((PageContext)getJspContext());
            if (sharebox) {
                pageContext.getRequest().setAttribute("showSharebox", true);
            }
            pageContext.include("/WEB-INF/jsp/forum/wall/wall.jsp");
        } catch (ServletException e) {
            throw new JspException(e);
        } finally {
            sharebox = false;
        }
    }

    public void setSharebox(boolean sharebox) {
        this.sharebox = sharebox;
    }
}

