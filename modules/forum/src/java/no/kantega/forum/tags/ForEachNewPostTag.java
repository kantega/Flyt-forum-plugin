package no.kantega.forum.tags;

import org.apache.log4j.Logger;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.jstl.core.LoopTagSupport;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import no.kantega.publishing.spring.RootContext;
import no.kantega.publishing.common.data.Content;
import no.kantega.forum.dao.ForumDao;

/**
 * User: Anders Skar, Kantega AS
 * Date: Jan 28, 2007
 * Time: 1:21:19 AM
 */
public class ForEachNewPostTag extends LoopTagSupport {

    private Iterator i = null;
    private int maxPosts = 5;
    private String forumId = null;

    private Logger log = Logger.getLogger(ForEachNewPostTag.class);


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

            long fId = -1;
            if (forumId != null) {
                try {
                    forumId = (String) ExpressionEvaluationUtils.evaluate("forumId", forumId, String.class, pageContext);
                    if (forumId != null) {
                        try {
                            fId = Long.parseLong(forumId, 10);
                        } catch (NumberFormatException e) {
                            
                        }
                    }
                } catch (JspException e) {
                    log.error(e);
                    throw new JspTagException(e.getMessage());
                }
            }

            List l;
            if (fId == -1) {
                l = dao.getLastPosts(maxPosts);
            } else {
                l = dao.getLastPostsInForum(fId, maxPosts);
            }

            i = l.iterator();
        }
    }

    public void setMaxPosts(int maxPosts) {
        this.maxPosts = maxPosts;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

}
