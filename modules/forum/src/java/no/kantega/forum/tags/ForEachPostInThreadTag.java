package no.kantega.forum.tags;

import no.kantega.publishing.spring.RootContext;
import no.kantega.publishing.common.data.Content;
import no.kantega.forum.dao.ForumDao;

import javax.servlet.jsp.jstl.core.LoopTagSupport;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.springframework.web.util.ExpressionEvaluationUtils;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Jun 12, 2006
 * Time: 3:49:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ForEachPostInThreadTag extends LoopTagSupport {

    private Iterator i = null;
    private String threadId = null;
    private int firstResult = 0;
    private int maxPosts = -1;
    private boolean reverse;

    private Logger log = Logger.getLogger(ForEachPostInThreadTag.class);


    protected Object next() throws JspTagException {
        return i == null ? null : i.next();
    }

    protected boolean hasNext() throws JspTagException {
        return i != null && i.hasNext();
    }

    protected void prepare() throws JspTagException {
        i = null;
        Map daos = RootContext.getInstance().getBeansOfType(ForumDao.class);
        if(daos.size() > 0) {
            ForumDao dao = (ForumDao) daos.values().iterator().next();

            long tId;
            if (threadId != null) {
                tId = Long.parseLong(threadId, 10);
            } else {
                HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();                
                Content content = (Content)request.getAttribute("aksess_this");
                tId = dao.getThreadAboutContent(content);
            }

            List l = new ArrayList();
            if (tId > 0) {
                l = dao.getPostsInThread(tId, firstResult, maxPosts, reverse);
            }
            i = l.iterator();
        }

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
}
