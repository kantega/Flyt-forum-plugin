package no.kantega.forum.tags.wall;


import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class RenderWallScriptsAndCssTag extends SimpleTagSupport{

    private int forumid = -1;
    private int maxthreads = 20;

    public void doTag() throws JspException, IOException {
        try {
            PageContext pageContext = ((PageContext)getJspContext());
            pageContext.getRequest().setAttribute("forumid", forumid);
            pageContext.getRequest().setAttribute("maxthreads", maxthreads);
            pageContext.include("/WEB-INF/jsp/forum/wall/scripts-and-css.jsp");
        } catch (ServletException e) {
            throw new JspException(e);
        } finally {
            forumid = -1;
            maxthreads = 20;
        }
    }

    public void setForumid(int forumid) {
        this.forumid = forumid;
    }

    public void setMaxthreads(int maxthreads) {
        this.maxthreads = maxthreads;
    }
}

