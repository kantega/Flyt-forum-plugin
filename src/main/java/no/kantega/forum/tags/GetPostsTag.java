package no.kantega.forum.tags;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.forum.util.ForumThreader;
import no.kantega.publishing.common.data.Content;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GetPostsTag extends SimpleTagSupport {

    private Content contentPage;
    private int maxPosts = -1;
    private long threadId = -1;
    private boolean threaded = false;

    private String var = "post";

    private ForumDao dao;

    @Override
    public void setJspContext(JspContext pc) {
        super.setJspContext(pc);
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(((PageContext) pc).getServletContext());
        dao = context.getBean(ForumDao.class);
    }


    public void doTag() throws JspException, IOException {
            List<Post> l = Collections.emptyList();
            if (contentPage != null) {
                threadId = dao.getThreadAboutContent(contentPage.getId());
                if (threadId != -1) {
                    l = getPostsInThread(dao);
                }
            } else if (threadId != -1) {
                l = getPostsInThread(dao);
            } else {
                l = dao.getLastPosts(maxPosts);
            }

            ((PageContext)getJspContext()).getRequest().setAttribute(var, l);

        maxPosts = -1;
        contentPage = null;
        threadId = -1;
        threaded = false;

        var = "post";

    }

    private List<Post> getPostsInThread(ForumDao dao) {
        List<Post> l = dao.getPostsInThread(threadId, 0, maxPosts, false);
        if (threaded) {
            ForumThreader ft = new ForumThreader();
            l = ft.organizePostsInThread(l);
        }
        return l;
    }

    public void setMaxPosts(int maxPosts) {
        this.maxPosts = maxPosts;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setContentPage(Content contentPage) {
        this.contentPage = contentPage;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public void setThreaded(boolean threaded) {
        this.threaded = threaded;
    }
}
