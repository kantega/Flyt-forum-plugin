package no.kantega.forum.tags.wall;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class RenderWallTag extends SimpleTagSupport{

    private boolean sharebox = false;
    private int forumid = -1;
    private int maxthreads = 20;
    private String userId;
    private int threadId = -1;

    public void doTag() throws JspException, IOException {
        try {
            PageContext pageContext = ((PageContext)getJspContext());
            if (sharebox) {
                pageContext.getRequest().setAttribute("showSharebox", true);
            }
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

            String forumListPostsUrl = request.getContextPath() + "/forum/listPosts?forumId=" + forumid + "&numberOfPostsToShow=" + maxthreads;
            if (userId != null) {
                forumListPostsUrl += "&username=" + userId;
            }
            if (threadId != -1) {
                forumListPostsUrl += "&threadId=" + threadId;
            }

            pageContext.getRequest().setAttribute("userid", userId);
            pageContext.getRequest().setAttribute("forumListPostsUrl", forumListPostsUrl);
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

    public void setForumid(int forumid) {
        this.forumid = forumid;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMaxthreads(int maxthreads) {
        this.maxthreads = maxthreads;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }
}

