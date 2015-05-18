package no.kantega.forum.tags;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.util.ForumThreader;
import no.kantega.publishing.common.data.Content;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.core.LoopTagSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ForEachPostInThreadTag extends LoopTagSupport {

    private Iterator i = null;
    private String threadId = null;
    private int firstResult = 0;
    private int maxPosts = -1;
    private boolean reverse;
    private boolean threaded = false;

    private Logger log = Logger.getLogger(ForEachPostInThreadTag.class);
    private ForumDao dao;

    @Override
    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext());
        dao = context.getBean(ForumDao.class);
    }

    protected Object next() throws JspTagException {
        return i == null ? null : i.next();
    }

    protected boolean hasNext() throws JspTagException {
        return i != null && i.hasNext();
    }

    protected void prepare() throws JspTagException {
        i = null;

        long tId;
        if (threadId != null) {
            tId = Long.parseLong(threadId, 10);
        } else {
            Content content = (Content)pageContext.getRequest().getAttribute("aksess_this");
            tId = dao.getThreadAboutContent(content.getId());
        }

        List l = new ArrayList();
        if (tId > 0) {
            l = dao.getPostsInThread(tId, firstResult, maxPosts, reverse);
            if (threaded) {
                ForumThreader ft = new ForumThreader();
                l = ft.organizePostsInThread(l);
            }
        }
        i = l.iterator();

        firstResult = 0;
        maxPosts = -1;
        reverse = false;
        threadId = null;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public void setMaxPosts(int maxPosts) {
        this.maxPosts = maxPosts;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public void setThreaded(boolean threaded) {
        this.threaded = threaded;
    }
}
